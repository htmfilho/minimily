import csv


def import_csv_file(csv_file_path):
    with open(csv_file_path) as csv_file_content:
        return import_csv_data(csv_file_content)


def import_csv_data(csv_file_content):
    csv_data = []
    reader = csv.DictReader(csv_file_content, delimiter=';')
    for row in reader:
        csv_data.append(row)
    return csv_data
