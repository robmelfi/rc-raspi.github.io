/* tslint:disable no-unused-expression */
import { browser, element, by } from 'protractor';

import NavBarPage from './../../page-objects/navbar-page';
import SignInPage from './../../page-objects/signin-page';
import NetatmoComponentsPage from './netatmo.page-object';
import { NetatmoDeleteDialog } from './netatmo.page-object';
import NetatmoUpdatePage from './netatmo-update.page-object';
import { waitUntilDisplayed, waitUntilHidden } from '../../util/utils';

const expect = chai.expect;

describe('Netatmo e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let netatmoUpdatePage: NetatmoUpdatePage;
  let netatmoComponentsPage: NetatmoComponentsPage;
  let netatmoDeleteDialog: NetatmoDeleteDialog;

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

  it('should load Netatmos', async () => {
    await navBarPage.getEntityPage('netatmo');
    netatmoComponentsPage = new NetatmoComponentsPage();
    expect(await netatmoComponentsPage.getTitle().getText()).to.match(/Netatmos/);
  });

  it('should load create Netatmo page', async () => {
    await netatmoComponentsPage.clickOnCreateButton();
    netatmoUpdatePage = new NetatmoUpdatePage();
    expect(await netatmoUpdatePage.getPageTitle().getAttribute('id')).to.match(/rcraspiApp.netatmo.home.createOrEditLabel/);
  });

  it('should create and save Netatmos', async () => {
    const nbButtonsBeforeCreate = await netatmoComponentsPage.countDeleteButtons();

    await netatmoUpdatePage.setClientIdInput('clientId');
    expect(await netatmoUpdatePage.getClientIdInput()).to.match(/clientId/);
    await netatmoUpdatePage.setClientSecretInput('clientSecret');
    expect(await netatmoUpdatePage.getClientSecretInput()).to.match(/clientSecret/);
    await netatmoUpdatePage.setEmailInput('email');
    expect(await netatmoUpdatePage.getEmailInput()).to.match(/email/);
    await netatmoUpdatePage.setPasswordInput('password');
    expect(await netatmoUpdatePage.getPasswordInput()).to.match(/password/);
    const selectedEnabled = await netatmoUpdatePage.getEnabledInput().isSelected();
    if (selectedEnabled) {
      await netatmoUpdatePage.getEnabledInput().click();
      expect(await netatmoUpdatePage.getEnabledInput().isSelected()).to.be.false;
    } else {
      await netatmoUpdatePage.getEnabledInput().click();
      expect(await netatmoUpdatePage.getEnabledInput().isSelected()).to.be.true;
    }
    await waitUntilDisplayed(netatmoUpdatePage.getSaveButton());
    await netatmoUpdatePage.save();
    await waitUntilHidden(netatmoUpdatePage.getSaveButton());
    expect(await netatmoUpdatePage.getSaveButton().isPresent()).to.be.false;

    await netatmoComponentsPage.waitUntilDeleteButtonsLength(nbButtonsBeforeCreate + 1);
    expect(await netatmoComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1);
  });

  it('should delete last Netatmo', async () => {
    await netatmoComponentsPage.waitUntilLoaded();
    const nbButtonsBeforeDelete = await netatmoComponentsPage.countDeleteButtons();
    await netatmoComponentsPage.clickOnLastDeleteButton();

    const deleteModal = element(by.className('modal'));
    await waitUntilDisplayed(deleteModal);

    netatmoDeleteDialog = new NetatmoDeleteDialog();
    expect(await netatmoDeleteDialog.getDialogTitle().getAttribute('id')).to.match(/rcraspiApp.netatmo.delete.question/);
    await netatmoDeleteDialog.clickOnConfirmButton();

    await netatmoComponentsPage.waitUntilDeleteButtonsLength(nbButtonsBeforeDelete - 1);
    expect(await netatmoComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
