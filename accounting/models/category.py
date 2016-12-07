from django.db import models
from django.contrib import admin


class CategoryAdmin(admin.ModelAdmin):
    list_display = ('name', 'portfolio', 'top_limit', 'parent')
    fieldsets = ((None, {'fields': ('name', 'portfolio', 'top_limit', 'parent')}),)


class Category(models.Model):
    name = models.CharField(max_length=100)
    portfolio = models.ForeignKey('Portfolio')
    top_limit = models.DecimalField(max_digits=8, decimal_places=2, blank=True, null=True)
    parent = models.ForeignKey('Category', blank=True, null=True)

    def children(self):
        return Category.objects.filter(parent=self).order_by('name')

    def siblings(self):
        return Category.objects.filter(parent=self.parent).exclude(pk=self.pk).order_by('name')

    def is_root(self):
        return False if self.parent else True


def find_by_portfolio(portfolio):
    return Category.objects.filter(portfolio=portfolio)


def build_full_tree(categories, parent):
    pass


def get_full_tree(portfolio):
    categories = list(find_by_portfolio(portfolio))
    return build_full_tree(categories)