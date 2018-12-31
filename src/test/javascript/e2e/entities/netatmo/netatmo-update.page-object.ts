import { element, by, ElementFinder } from 'protractor';

export default class NetatmoUpdatePage {
  pageTitle: ElementFinder = element(by.id('rcraspiApp.netatmo.home.createOrEditLabel'));
  saveButton: ElementFinder = element(by.id('save-entity'));
  cancelButton: ElementFinder = element(by.id('cancel-save'));
  clientIdInput: ElementFinder = element(by.css('input#netatmo-clientId'));
  clientSecretInput: ElementFinder = element(by.css('input#netatmo-clientSecret'));
  emailInput: ElementFinder = element(by.css('input#netatmo-email'));
  passwordInput: ElementFinder = element(by.css('input#netatmo-password'));
  enabledInput: ElementFinder = element(by.css('input#netatmo-enabled'));

  getPageTitle() {
    return this.pageTitle;
  }

  async setClientIdInput(clientId) {
    await this.clientIdInput.sendKeys(clientId);
  }

  async getClientIdInput() {
    return this.clientIdInput.getAttribute('value');
  }

  async setClientSecretInput(clientSecret) {
    await this.clientSecretInput.sendKeys(clientSecret);
  }

  async getClientSecretInput() {
    return this.clientSecretInput.getAttribute('value');
  }

  async setEmailInput(email) {
    await this.emailInput.sendKeys(email);
  }

  async getEmailInput() {
    return this.emailInput.getAttribute('value');
  }

  async setPasswordInput(password) {
    await this.passwordInput.sendKeys(password);
  }

  async getPasswordInput() {
    return this.passwordInput.getAttribute('value');
  }

  getEnabledInput() {
    return this.enabledInput;
  }
  async save() {
    await this.saveButton.click();
  }

  async cancel() {
    await this.cancelButton.click();
  }

  getSaveButton() {
    return this.saveButton;
  }
}
