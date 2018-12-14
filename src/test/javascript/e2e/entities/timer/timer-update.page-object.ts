import { element, by, ElementFinder } from 'protractor';

export default class TimerUpdatePage {
  pageTitle: ElementFinder = element(by.id('rcraspiApp.timer.home.createOrEditLabel'));
  saveButton: ElementFinder = element(by.id('save-entity'));
  cancelButton: ElementFinder = element(by.id('cancel-save'));
  nameInput: ElementFinder = element(by.css('input#timer-name'));
  startInput: ElementFinder = element(by.css('input#timer-start'));
  stopInput: ElementFinder = element(by.css('input#timer-stop'));
  repeatSelect: ElementFinder = element(by.css('select#timer-repeat'));

  getPageTitle() {
    return this.pageTitle;
  }

  async setNameInput(name) {
    await this.nameInput.sendKeys(name);
  }

  async getNameInput() {
    return this.nameInput.getAttribute('value');
  }

  async setStartInput(start) {
    await this.startInput.sendKeys(start);
  }

  async getStartInput() {
    return this.startInput.getAttribute('value');
  }

  async setStopInput(stop) {
    await this.stopInput.sendKeys(stop);
  }

  async getStopInput() {
    return this.stopInput.getAttribute('value');
  }

  async setRepeatSelect(repeat) {
    await this.repeatSelect.sendKeys(repeat);
  }

  async getRepeatSelect() {
    return this.repeatSelect.element(by.css('option:checked')).getText();
  }

  async repeatSelectLastOption() {
    await this.repeatSelect
      .all(by.tagName('option'))
      .last()
      .click();
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
