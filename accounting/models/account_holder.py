from django.db import models


class AccountHolder(models.Model):
    person = models.ForeignKey('base.Person')
    account = models.ForeignKey('Account')
