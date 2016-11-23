from django.shortcuts import render
from accounting.forms.import_bank_file import ImportBankFileForm
from accounting.models.transaction import BankDataImport


def view_import_bank_file(request):
    form = ImportBankFileForm()
    if request.method == 'POST':
        try:
            _handle_post(request)
            return render(request, 'accounting_home.html', locals())
        except Exception as exception:
            return render(request, 'import_bank_file.html', locals())
    else:
        return render(request, 'import_bank_file.html', locals())


def _handle_post(request):
    form = ImportBankFileForm(request.POST, request.FILES)
    if form.is_valid():
        bank_file = form.cleaned_data.get('bank_file')
        bank_data_import = BankDataImport()
        bank_data_import.load_csv_file(bank_file.temporary_file_path())
        bank_data_import.strip_data()
        bank_data_import.import_data()
