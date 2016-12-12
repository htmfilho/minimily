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
    children = []

    def get_children(self):
        if self.children:
            return self.children
        else:
            self.children = Category.objects.filter(parent=self).order_by('name')
            return self.children

    def siblings(self):
        return Category.objects.filter(parent=self.parent).exclude(pk=self.pk).order_by('name')

    def is_root(self):
        return False if self.parent else True


def find_by_portfolio(portfolio):
    return Category.objects.filter(portfolio=portfolio)


def build_tree(a_list, parent):
    children = []
    for element in a_list:
        if element.parent is parent:
            children.append(element)
            element.children = build_tree(a_list, element)
    return children


def build_forest(a_list):
    roots = []
    for element in a_list:
        if element.parent is None:
            roots.append(element)
            element.children = build_tree(a_list, element)
    return roots


def get_tree_categories(portfolio):
    categories = list(find_by_portfolio(portfolio))
    return build_forest(categories)