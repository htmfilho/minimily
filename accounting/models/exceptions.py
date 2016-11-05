class BankDataImportValidationException(Exception):
    def __init__(self, message=None):
        super(BankDataImportValidationException, self).__init__(message)


class InexistingAccountException(Exception):
    def __init__(self, message=None):
        super(InexistingAccountException, self).__init__(message)
