from django import forms


class ImportBankFileForm(forms.Form):
    bank_file = forms.FileField(label="Bank File", required=True)
