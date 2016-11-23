import datetime
from django.db import models
from django.contrib import admin
from accounting.utils import csv_import
from . import account, exceptions

TRANSACTION_TYPES = (('CREDIT', 'Credit'),
                     ('DEBIT', 'Debit'))


class TransactionAdmin(admin.ModelAdmin):
    list_display = ('account', 'counterparty', 'type', 'amount', 'value_date', 'booking_date')
    fieldsets = ((None, {'fields': ('account', 'counterparty', 'type', 'amount', 'value_date', 'booking_date',
                                    'description', 'details', 'message')}),)


class Transaction(models.Model):
    account = models.ForeignKey('Account')
    counterparty = models.ForeignKey('Account', related_name='counterparty', null=True, blank=True)
    type = models.CharField(max_length=10, choices=TRANSACTION_TYPES)
    amount = models.DecimalField(max_digits=8, decimal_places=2)
    value_date = models.DateTimeField()
    booking_date = models.DateField(null=True, blank=True)
    description = models.TextField(null=True, blank=True)
    details = models.TextField(null=True, blank=True)
    message = models.TextField(null=True, blank=True)


def find_by_account(an_account):
    return Transaction.objects.filter(account=an_account)


class BankDataImport:
    ACCOUNT_NUMBER = 'Account Number'
    ACCOUNT_NAME = 'Account Name'
    COUNTERPARTY_ACCOUNT = 'Counterparty account'
    ENTRY_NUMBER = 'Entry number'
    BOOKING_DATE = 'Booking date'
    VALUE_DATE = 'Value date'
    AMOUNT = 'Amount'
    CURRENCY = 'Currency'
    DESCRIPTION = 'Description'
    ENTRY_DETAILS = 'Entry Details'
    MESSAGE = 'Message'

    DATE_FORMAT = '%d/%m/%Y'

    fields = [ACCOUNT_NUMBER, ACCOUNT_NAME, COUNTERPARTY_ACCOUNT, ENTRY_NUMBER, BOOKING_DATE,
              VALUE_DATE, AMOUNT, CURRENCY, DESCRIPTION, ENTRY_DETAILS, MESSAGE]

    csv_data = None

    def __init__(self):
        self.fields.sort()

    def load_csv_file(self, csv_file_path):
        self.csv_data = csv_import.import_csv_file(csv_file_path)
        if not self._are_fields_valid():
            raise exceptions.BankDataImportValidationException()

    def load_csv_data(self, csv_file_content):
        self.csv_data = csv_import.import_csv_data(csv_file_content)
        if not self._are_fields_valid():
            raise exceptions.BankDataImportValidationException()

    def strip_data(self):
        for row in self.csv_data:
            for field in self.fields:
                row[field] = row[field].strip()

    def import_data(self):
        for csv_row in self.csv_data:
            an_account = account.get_by_reference(csv_row[self.ACCOUNT_NUMBER])
            if an_account:
                a_transaction = self._create_transaction(an_account, csv_row)
                if a_transaction:
                    a_transaction.save()
            else:
                raise exceptions.InexistingAccountException(csv_row[self.ACCOUNT_NUMBER])

    def _are_fields_valid(self):
        row = self.csv_data[0]
        actual_fields = list(row.keys())
        actual_fields.sort()
        return self.fields == actual_fields

    def _create_transaction(self, an_account, csv_row):
        counterparty = self._get_counterparty(csv_row[self.COUNTERPARTY_ACCOUNT])
        if csv_row[self.AMOUNT]:
            amount = float(csv_row[self.AMOUNT].replace(',', '.'))
            transaction_type = TRANSACTION_TYPES[0][0] if amount >= 0 else TRANSACTION_TYPES[1][0]
            value_date = datetime.datetime.strptime(csv_row[self.VALUE_DATE], self.DATE_FORMAT).date()
            booking_date = datetime.datetime.strptime(csv_row[self.BOOKING_DATE], self.DATE_FORMAT).date()
            return Transaction(account=an_account, counterparty=counterparty, type=transaction_type,
                               amount=amount, value_date=value_date, booking_date=booking_date,
                               description=csv_row[self.DESCRIPTION], details=csv_row[self.ENTRY_DETAILS],
                               message=csv_row[self.MESSAGE])
        else:
            return None

    def _get_counterparty(self, reference):
        counterparty = account.get_by_reference(reference)
        if not counterparty:
            counterparty = account.Account(name=reference, reference=reference)
            counterparty.save()
        return counterparty