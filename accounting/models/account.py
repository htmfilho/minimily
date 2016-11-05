from django.db import models


class Account(models.Model):
    name = models.TextField(max_length=100)
    reference = models.CharField(max_length=30, null=True, blank=True)


def get_by_reference(reference):
    try:
        return Account.objects.get(reference=reference)
    except Account.DoesNotExist:
        return None
