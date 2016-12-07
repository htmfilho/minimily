from django.db import models
from django.contrib import admin


class AccountAdmin(admin.ModelAdmin):
    list_display = ('name', 'reference', 'portfolio')
    fieldsets = ((None, {'fields': ('name', 'reference', 'portfolio')}),)


class Account(models.Model):
    name = models.CharField(max_length=100)
    portfolio = models.ForeignKey('Portfolio', null=True, blank=True)
    reference = models.CharField(max_length=30, null=True, blank=True)

    def __str__(self):
        return self.reference


def get_by_reference(reference):
    try:
        return Account.objects.get(reference=reference)
    except Account.DoesNotExist:
        return None
