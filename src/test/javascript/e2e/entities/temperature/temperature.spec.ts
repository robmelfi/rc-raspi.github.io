/* tslint:disable no-unused-expression */
import { browser, element, by, protractor } from 'protractor';

import NavBarPage from './../../page-objects/navbar-page';
import SignInPage from './../../page-objects/signin-page';
import TemperatureComponentsPage from './temperature.page-object';
import { TemperatureDeleteDialog } from './temperature.page-object';
import TemperatureUpdatePage from './temperature-update.page-object';
import { waitUntilDisplayed, waitUntilHidden } from '../../util/utils';

const expect = chai.expect;

describe('Temperature e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let temperatureUpdatePage: TemperatureUpdatePage;
  let temperatureComponentsPage: TemperatureComponentsPage;
  let temperatureDeleteDialog: TemperatureDeleteDialog;

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.waitUntilDisplayed();

    await signInPage.username.sendKeys('admin');
    await signInPage.password.sendKeys('admin');
    await signInPage.loginButton.click();
    await signInPage.waitUntilHidden();

    await waitUntilDisplayed(navBarPage.entityMenu);
  });

  it('should load Temperatures', async () => {
    await navBarPage.getEntityPage('temperature');
    temperatureComponentsPage = new TemperatureComponentsPage();
    expect(await temperatureComponentsPage.getTitle().getText()).to.match(/Temperatures/);
  });

  it('should load create Temperature page', async () => {
    await temperatureComponentsPage.clickOnCreateButton();
    temperatureUpdatePage = new TemperatureUpdatePage();
    expect(await temperatureUpdatePage.getPageTitle().getAttribute('id')).to.match(/rcraspiApp.temperature.home.createOrEditLabel/);
  });

  it('should create and save Temperatures', async () => {
    const nbButtonsBeforeCreate = await temperatureComponentsPage.countDeleteButtons();

    await temperatureUpdatePage.setValueInput('5');
    expect(await temperatureUpdatePage.getValueInput()).to.eq('5');
    await temperatureUpdatePage.setTimestampInput('01/01/2001' + protractor.Key.TAB + '02:30AM');
    expect(await temperatureUpdatePage.getTimestampInput()).to.contain('2001-01-01T02:30');
    await waitUntilDisplayed(temperatureUpdatePage.getSaveButton());
    await temperatureUpdatePage.save();
    await waitUntilHidden(temperatureUpdatePage.getSaveButton());
    expect(await temperatureUpdatePage.getSaveButton().isPresent()).to.be.false;

    await temperatureComponentsPage.waitUntilDeleteButtonsLength(nbButtonsBeforeCreate + 1);
    expect(await temperatureComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1);
  });

  it('should delete last Temperature', async () => {
    await temperatureComponentsPage.waitUntilLoaded();
    const nbButtonsBeforeDelete = await temperatureComponentsPage.countDeleteButtons();
    await temperatureComponentsPage.clickOnLastDeleteButton();

    const deleteModal = element(by.className('modal'));
    await waitUntilDisplayed(deleteModal);

    temperatureDeleteDialog = new TemperatureDeleteDialog();
    expect(await temperatureDeleteDialog.getDialogTitle().getAttribute('id')).to.match(/rcraspiApp.temperature.delete.question/);
    await temperatureDeleteDialog.clickOnConfirmButton();

    await temperatureComponentsPage.waitUntilDeleteButtonsLength(nbButtonsBeforeDelete - 1);
    expect(await temperatureComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
