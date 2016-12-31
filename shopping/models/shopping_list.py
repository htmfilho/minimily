from django.db import models
from django.contrib import admin


class ShoppingListAdmin(admin.ModelAdmin):
    list_display = ('name', 'quantity', 'product', 'listed_date')
    fieldsets = ((None, {'fields': ('name', 'product', 'quantity')}),)


class ShoppingList(models.Model):
    name = models.CharField(max_length=256)
    quantity = models.DecimalField(max_digits=10, decimal_places=3, blank=True, null=True)
    product = models.ForeignKey('Product', blank=True, null=True)
    listed_date = models.DateTimeField(auto_now_add=True)

    def __str__(self):
        return self.name
