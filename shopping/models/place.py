from django.db import models
from django.contrib import admin


class PlaceAdmin(admin.ModelAdmin):
    list_display = ('name', 'location', 'city', 'phone')
    fieldsets = ((None, {'fields': ('name', 'location', 'city', 'phone')}),)


class Place(models.Model):
    name = models.CharField(max_length=50)
    location = models.CharField(max_length=255)  # street, avenue, number, box, apto, etc.
    city = models.CharField(max_length=100)
    phone = models.CharField(max_length=20, blank=True, null=True)

    def __str__(self):
        return self.name
