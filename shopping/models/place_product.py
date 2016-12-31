from django.db import models
from django.contrib import admin


class PlaceProductAdmin(admin.ModelAdmin):
    list_display = ('place', 'reference', 'product', 'name', 'bio', 'rate')
    fieldsets = ((None, {'fields': ('place', 'product', 'reference', 'name', 'bio', 'rate')}),)
    list_filter = ('bio', 'rate')


class PlaceProduct(models.Model):
    RATE_CHOICES = (('VERY_BAD', 'Very Bad'),
                    ('BAD', 'Bad'),
                    ('REGULAR', 'Regular'),
                    ('GOOD', 'Good'),
                    ('VERY_GOOD', 'Very Good'))

    place = models.ForeignKey('Place')
    name = models.CharField(max_length=100)
    product = models.ForeignKey('Product', blank=True, null=True)
    reference = models.CharField(max_length=20, blank=True, null=True)
    bio = models.BooleanField(default=False)
    rate = models.CharField(max_length=10, blank=True, null=True, choices=RATE_CHOICES)

    def __str__(self):
        return u"%s %s" % (self.reference, self.name)
