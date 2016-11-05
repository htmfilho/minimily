import csv


def import_csv_data(csv_file_path):
    csv_data = []

    with open(csv_file_path) as csvfile:
        reader = csv.DictReader(csvfile, delimiter=';')
        for row in reader:
            csv_data.append(row)

    return csv_data
