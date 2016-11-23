from django.conf.urls import url
from .views import bank, home

urlpatterns = [
    url(r'^$', home.view_home, name='accounting_home'),
    url(r'bank/import/$', bank.view_import_bank_file, name='accounting_import_bank_file'),
]
