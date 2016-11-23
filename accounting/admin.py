from django.contrib import admin
from accounting.models import account, transaction

admin.site.register(account.Account, account.AccountAdmin)
admin.site.register(transaction.Transaction, transaction.TransactionAdmin)