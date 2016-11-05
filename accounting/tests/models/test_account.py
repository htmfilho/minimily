from django.test import TestCase
from accounting.models import account


class AccountTest(TestCase):
    def test_find_by_reference(self):
        self.assertEqual(account.get_by_reference('ref'), None, 'When an object is not fould it should return None')
