from django.db import models
from django.contrib import admin


class PortfolioAdmin(admin.ModelAdmin):
    list_display = ('name', 'person')
    fieldsets = ((None, {'fields': ('person', 'name')}),)


class Portfolio(models.Model):
    name = models.CharField(max_length=100)
    person = models.ForeignKey('base.Person')

    def __str__(self):
        return self.name
