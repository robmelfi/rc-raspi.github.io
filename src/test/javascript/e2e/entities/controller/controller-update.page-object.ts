import { element, by, ElementFinder } from 'protractor';

export default class ControllerUpdatePage {
  pageTitle: ElementFinder = element(by.id('rcraspiApp.controller.home.createOrEditLabel'));
  saveButton: ElementFinder = element(by.id('save-entity'));
  cancelButton: ElementFinder = element(by.id('cancel-save'));
  nameInput: ElementFinder = element(by.css('input#controller-name'));
  modeSelect: ElementFinder = element(by.css('select#controller-mode'));
  pinSelect: ElementFinder = element(by.css('select#controller-pin'));

  getPageTitle() {
    return this.pageTitle;
  }

  async setNameInput(name) {
    await this.nameInput.sendKeys(name);
  }

  async getNameInput() {
    return this.nameInput.getAttribute('value');
  }

  async setModeSelect(mode) {
    await this.modeSelect.sendKeys(mode);
  }

  async getModeSelect() {
    return this.modeSelect.element(by.css('option:checked')).getText();
  }

  async modeSelectLastOption() {
    await this.modeSelect
      .all(by.tagName('option'))
      .last()
      .click();
  }
  async pinSelectLastOption() {
    await this.pinSelect
      .all(by.tagName('option'))
      .last()
      .click();
  }

  async pinSelectOption(option) {
    await this.pinSelect.sendKeys(option);
  }

  getPinSelect() {
    return this.pinSelect;
  }

  async getPinSelectedOption() {
    return this.pinSelect.element(by.css('option:checked')).getText();
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
