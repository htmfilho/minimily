from django.db import models
from django.contrib import admin


class ProductAdmin(admin.ModelAdmin):
    list_display = ('name',)
    fieldsets = ((None, {'fields': ('name',)}),)


class Product(models.Model):
    name = models.CharField(max_length=50)

    def __str__(self):
        return self.name
