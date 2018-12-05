/* tslint:disable no-unused-expression */
import { browser, element, by, protractor } from 'protractor';

import NavBarPage from './../../page-objects/navbar-page';
import SignInPage from './../../page-objects/signin-page';
import HumidityComponentsPage from './humidity.page-object';
import { HumidityDeleteDialog } from './humidity.page-object';
import HumidityUpdatePage from './humidity-update.page-object';
import { waitUntilDisplayed, waitUntilHidden } from '../../util/utils';

const expect = chai.expect;

describe('Humidity e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let humidityUpdatePage: HumidityUpdatePage;
  let humidityComponentsPage: HumidityComponentsPage;
  let humidityDeleteDialog: HumidityDeleteDialog;

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

  it('should load Humidities', async () => {
    await navBarPage.getEntityPage('humidity');
    humidityComponentsPage = new HumidityComponentsPage();
    expect(await humidityComponentsPage.getTitle().getText()).to.match(/Humidities/);
  });

  it('should load create Humidity page', async () => {
    await humidityComponentsPage.clickOnCreateButton();
    humidityUpdatePage = new HumidityUpdatePage();
    expect(await humidityUpdatePage.getPageTitle().getAttribute('id')).to.match(/rcraspiApp.humidity.home.createOrEditLabel/);
  });

  it('should create and save Humidities', async () => {
    const nbButtonsBeforeCreate = await humidityComponentsPage.countDeleteButtons();

    await humidityUpdatePage.setValueInput('5');
    expect(await humidityUpdatePage.getValueInput()).to.eq('5');
    await humidityUpdatePage.setTimestampInput('01/01/2001' + protractor.Key.TAB + '02:30AM');
    expect(await humidityUpdatePage.getTimestampInput()).to.contain('2001-01-01T02:30');
    await waitUntilDisplayed(humidityUpdatePage.getSaveButton());
    await humidityUpdatePage.save();
    await waitUntilHidden(humidityUpdatePage.getSaveButton());
    expect(await humidityUpdatePage.getSaveButton().isPresent()).to.be.false;

    await humidityComponentsPage.waitUntilDeleteButtonsLength(nbButtonsBeforeCreate + 1);
    expect(await humidityComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1);
  });

  it('should delete last Humidity', async () => {
    await humidityComponentsPage.waitUntilLoaded();
    const nbButtonsBeforeDelete = await humidityComponentsPage.countDeleteButtons();
    await humidityComponentsPage.clickOnLastDeleteButton();

    const deleteModal = element(by.className('modal'));
    await waitUntilDisplayed(deleteModal);

    humidityDeleteDialog = new HumidityDeleteDialog();
    expect(await humidityDeleteDialog.getDialogTitle().getAttribute('id')).to.match(/rcraspiApp.humidity.delete.question/);
    await humidityDeleteDialog.clickOnConfirmButton();

    await humidityComponentsPage.waitUntilDeleteButtonsLength(nbButtonsBeforeDelete - 1);
    expect(await humidityComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
