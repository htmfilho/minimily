from django.db import models
from django.contrib import admin


class ShoppingItemAdmin(admin.ModelAdmin):
    list_display = ('product', 'quantity', 'unit_price', 'discount', 'total', 'purchase_date')
    fieldsets = ((None, {'fields': ('product', 'purchase_date', 'quantity', 'unit_price', 'discount')}),)
    list_filter = ('purchase_date',)


class ShoppingItem(models.Model):
    product = models.ForeignKey('PlaceProduct')
    list = models.ForeignKey('ShoppingList', blank=True, null=True)
    purchase_date = models.DateTimeField(blank=True, null=True)
    quantity = models.DecimalField(max_digits=10, decimal_places=3, blank=True, null=True)
    unit_price = models.DecimalField(max_digits=10, decimal_places=3, blank=True, null=True)
    discount = models.DecimalField(max_digits=10, decimal_places=3, blank=True, null=True)

    @property
    def total(self):
        the_total = 0
        if self.unit_price:
            the_total = self.unit_price

        if self.quantity:
            the_total *= self.quantity

        if self.discount:
            the_total -= self.discount

        return the_total

    def __str__(self):
        return self.product.name
