/* tslint:disable no-unused-expression */
import { browser, element, by } from 'protractor';

import NavBarPage from './../../page-objects/navbar-page';
import SignInPage from './../../page-objects/signin-page';
import PinComponentsPage from './pin.page-object';
import { PinDeleteDialog } from './pin.page-object';
import PinUpdatePage from './pin-update.page-object';
import { waitUntilDisplayed, waitUntilHidden } from '../../util/utils';

const expect = chai.expect;

describe('Pin e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let pinUpdatePage: PinUpdatePage;
  let pinComponentsPage: PinComponentsPage;
  let pinDeleteDialog: PinDeleteDialog;

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

  it('should load Pins', async () => {
    await navBarPage.getEntityPage('pin');
    pinComponentsPage = new PinComponentsPage();
    expect(await pinComponentsPage.getTitle().getText()).to.match(/Pins/);
  });

  it('should load create Pin page', async () => {
    await pinComponentsPage.clickOnCreateButton();
    pinUpdatePage = new PinUpdatePage();
    expect(await pinUpdatePage.getPageTitle().getAttribute('id')).to.match(/rcraspiApp.pin.home.createOrEditLabel/);
  });

  it('should create and save Pins', async () => {
    const nbButtonsBeforeCreate = await pinComponentsPage.countDeleteButtons();

    await pinUpdatePage.setNameInput('name');
    expect(await pinUpdatePage.getNameInput()).to.match(/name/);
    await waitUntilDisplayed(pinUpdatePage.getSaveButton());
    await pinUpdatePage.save();
    await waitUntilHidden(pinUpdatePage.getSaveButton());
    expect(await pinUpdatePage.getSaveButton().isPresent()).to.be.false;

    await pinComponentsPage.waitUntilDeleteButtonsLength(nbButtonsBeforeCreate + 1);
    expect(await pinComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1);
  });

  it('should delete last Pin', async () => {
    await pinComponentsPage.waitUntilLoaded();
    const nbButtonsBeforeDelete = await pinComponentsPage.countDeleteButtons();
    await pinComponentsPage.clickOnLastDeleteButton();

    const deleteModal = element(by.className('modal'));
    await waitUntilDisplayed(deleteModal);

    pinDeleteDialog = new PinDeleteDialog();
    expect(await pinDeleteDialog.getDialogTitle().getAttribute('id')).to.match(/rcraspiApp.pin.delete.question/);
    await pinDeleteDialog.clickOnConfirmButton();

    await pinComponentsPage.waitUntilDeleteButtonsLength(nbButtonsBeforeDelete - 1);
    expect(await pinComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
