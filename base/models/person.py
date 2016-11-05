from django.db import models
from django.contrib.auth.models import User


class Person(models.Model):
    GENDERS = (('FEMALE', 'Female'), ('MALE', 'Male'))

    user = models.OneToOneField(User, blank=True, null=True)
    gender = models.CharField(max_length=10, choices=GENDERS)
