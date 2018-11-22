/* tslint:disable no-unused-expression */
import { browser, element, by } from 'protractor';

import NavBarPage from './../../page-objects/navbar-page';
import SignInPage from './../../page-objects/signin-page';
import ControllerComponentsPage from './controller.page-object';
import { ControllerDeleteDialog } from './controller.page-object';
import ControllerUpdatePage from './controller-update.page-object';
import { waitUntilDisplayed, waitUntilHidden } from '../../util/utils';

const expect = chai.expect;

describe('Controller e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let controllerUpdatePage: ControllerUpdatePage;
  let controllerComponentsPage: ControllerComponentsPage;
  let controllerDeleteDialog: ControllerDeleteDialog;

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

  it('should load Controllers', async () => {
    await navBarPage.getEntityPage('controller');
    controllerComponentsPage = new ControllerComponentsPage();
    expect(await controllerComponentsPage.getTitle().getText()).to.match(/Controllers/);
  });

  it('should load create Controller page', async () => {
    await controllerComponentsPage.clickOnCreateButton();
    controllerUpdatePage = new ControllerUpdatePage();
    expect(await controllerUpdatePage.getPageTitle().getAttribute('id')).to.match(/rcraspiApp.controller.home.createOrEditLabel/);
  });

  it('should create and save Controllers', async () => {
    const nbButtonsBeforeCreate = await controllerComponentsPage.countDeleteButtons();

    await controllerUpdatePage.setNameInput('name');
    expect(await controllerUpdatePage.getNameInput()).to.match(/name/);
    await controllerUpdatePage.modeSelectLastOption();
    await controllerUpdatePage.pinSelectLastOption();
    await waitUntilDisplayed(controllerUpdatePage.getSaveButton());
    await controllerUpdatePage.save();
    await waitUntilHidden(controllerUpdatePage.getSaveButton());
    expect(await controllerUpdatePage.getSaveButton().isPresent()).to.be.false;

    await controllerComponentsPage.waitUntilDeleteButtonsLength(nbButtonsBeforeCreate + 1);
    expect(await controllerComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1);
  });

  it('should delete last Controller', async () => {
    await controllerComponentsPage.waitUntilLoaded();
    const nbButtonsBeforeDelete = await controllerComponentsPage.countDeleteButtons();
    await controllerComponentsPage.clickOnLastDeleteButton();

    const deleteModal = element(by.className('modal'));
    await waitUntilDisplayed(deleteModal);

    controllerDeleteDialog = new ControllerDeleteDialog();
    expect(await controllerDeleteDialog.getDialogTitle().getAttribute('id')).to.match(/rcraspiApp.controller.delete.question/);
    await controllerDeleteDialog.clickOnConfirmButton();

    await controllerComponentsPage.waitUntilDeleteButtonsLength(nbButtonsBeforeDelete - 1);
    expect(await controllerComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
