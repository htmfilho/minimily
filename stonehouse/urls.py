from django.conf.urls import url, include
from django.contrib import admin

urlpatterns = [
    url(r'', include('base.urls')),
    url(r'^admin/', admin.site.urls),
    url(r'^accounting/', include('accounting.urls'))
]
