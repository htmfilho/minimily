from selenium import webdriver
from selenium.common.exceptions import WebDriverException


def test_application_running():
    browser = webdriver.Firefox()
    try:
        browser.get('http://localhost:8000')
        assert 'Stonehouse' in browser.title
    except WebDriverException:
        print('Application is not running')
    browser.quit()


test_application_running()

