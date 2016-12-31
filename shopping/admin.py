from django.contrib import admin
from shopping.models import place, place_product, shopping_item, shopping_list


admin.site.register(place.Place, place.PlaceAdmin)
admin.site.register(place_product.PlaceProduct, place_product.PlaceProductAdmin)
admin.site.register(shopping_list.ShoppingList, shopping_list.ShoppingListAdmin)
admin.site.register(shopping_item.ShoppingItem, shopping_item.ShoppingItemAdmin)
