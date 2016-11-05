from django.test import TestCase
from accounting.models import transaction, account, exceptions
from accounting.utils import csv_import


class BankDataImportTest(TestCase):
    csv_data = []

    CORRECT_CSV_FILE = 'accounting/tests/resources/bank_transactions.csv'
    FAILING_CSV_FILE = 'accounting/tests/resources/bank_transactions_wrong.csv'

    @classmethod
    def setUpTestData(cls):
        cls.csv_data = csv_import.import_csv_data(cls.CORRECT_CSV_FILE)

    def test_field_names(self):
        try:
            transaction.BankDataImport(self.FAILING_CSV_FILE)
            self.fail('The field names of the csv file are not correct.')
        except exceptions.BankDataImportValidationException:
            pass

    def test_stripped_data(self):
        unstripped_row = self.csv_data[0]

        bank_data_import = transaction.BankDataImport(self.CORRECT_CSV_FILE)
        bank_data_import.strip_data()
        self.assertFalse(unstripped_row['Description'] == bank_data_import.csv_data[0]['Description'],
                         "Fields may have useless spaces that can be ignored.")

    def test_import_without_account(self):
        bank_data_import = transaction.BankDataImport(self.CORRECT_CSV_FILE)
        self.assertRaises(exceptions.InexistingAccountException, bank_data_import.import_data)

    def test_import_with_counterparty(self):
        _create_account()
        _perform_bank_data_import(self.CORRECT_CSV_FILE)

        counterparty = account.get_by_reference('362-3046315-89')
        self.assertIsNotNone(counterparty, 'When a conterparty does not exist then the import should create it.')

    def test_import_consistency(self):
        _create_account()
        _perform_bank_data_import(self.CORRECT_CSV_FILE)

        self.assertEqual(transaction.Transaction.objects.all().count(), len(self.csv_data),
                         'The number of records in the database is different from the rows in the csv file.')

    def test_transaction_type(self):
        an_account = _create_account()
        _perform_bank_data_import(self.CORRECT_CSV_FILE)

        transactions = transaction.find_by_account(an_account)
        self.assertEqual(transactions[0].type, 'CREDIT', 'The first transaction is expected to be a credit one.')
        self.assertEqual(transactions[1].type, 'DEBIT', 'The first transaction is expected to be a debit one.')


def _create_account():
    an_account = account.Account(name='Leandro Fortunato Alencar', reference='363-7189318-41')
    an_account.save()
    return an_account


def _perform_bank_data_import(csv_file):
    bank_data_import = transaction.BankDataImport(csv_file)
    bank_data_import.import_data()
