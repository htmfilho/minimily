from django.db import models
from django.contrib import admin


class AccountAdmin(admin.ModelAdmin):
    list_display = ('name', 'reference')
    fieldsets = ((None, {'fields': ('name', 'reference')}),)


class Account(models.Model):
    name = models.CharField(max_length=100)
    reference = models.CharField(max_length=30, null=True, blank=True)

    def __str__(self):
        return self.reference


def get_by_reference(reference):
    try:
        return Account.objects.get(reference=reference)
    except Account.DoesNotExist:
        return None
