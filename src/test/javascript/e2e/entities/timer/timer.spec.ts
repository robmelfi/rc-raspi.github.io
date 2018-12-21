/* tslint:disable no-unused-expression */
import { browser, element, by, protractor } from 'protractor';

import NavBarPage from './../../page-objects/navbar-page';
import SignInPage from './../../page-objects/signin-page';
import TimerComponentsPage from './timer.page-object';
import { TimerDeleteDialog } from './timer.page-object';
import TimerUpdatePage from './timer-update.page-object';
import { waitUntilDisplayed, waitUntilHidden } from '../../util/utils';

const expect = chai.expect;

describe('Timer e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let timerUpdatePage: TimerUpdatePage;
  let timerComponentsPage: TimerComponentsPage;
  let timerDeleteDialog: TimerDeleteDialog;

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

  it('should load Timers', async () => {
    await navBarPage.getEntityPage('timer');
    timerComponentsPage = new TimerComponentsPage();
    expect(await timerComponentsPage.getTitle().getText()).to.match(/Timers/);
  });

  it('should load create Timer page', async () => {
    await timerComponentsPage.clickOnCreateButton();
    timerUpdatePage = new TimerUpdatePage();
    expect(await timerUpdatePage.getPageTitle().getAttribute('id')).to.match(/rcraspiApp.timer.home.createOrEditLabel/);
  });

  it('should create and save Timers', async () => {
    const nbButtonsBeforeCreate = await timerComponentsPage.countDeleteButtons();

    await timerUpdatePage.setNameInput('name');
    expect(await timerUpdatePage.getNameInput()).to.match(/name/);
    await timerUpdatePage.setStartInput('01/01/2001' + protractor.Key.TAB + '02:30AM');
    expect(await timerUpdatePage.getStartInput()).to.contain('2001-01-01T02:30');
    await timerUpdatePage.setStopInput('01/01/2001' + protractor.Key.TAB + '02:30AM');
    expect(await timerUpdatePage.getStopInput()).to.contain('2001-01-01T02:30');
    await timerUpdatePage.repeatSelectLastOption();
    await waitUntilDisplayed(timerUpdatePage.getSaveButton());
    await timerUpdatePage.save();
    await waitUntilHidden(timerUpdatePage.getSaveButton());
    expect(await timerUpdatePage.getSaveButton().isPresent()).to.be.false;

    await timerComponentsPage.waitUntilDeleteButtonsLength(nbButtonsBeforeCreate + 1);
    expect(await timerComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1);
  });

  it('should delete last Timer', async () => {
    await timerComponentsPage.waitUntilLoaded();
    const nbButtonsBeforeDelete = await timerComponentsPage.countDeleteButtons();
    await timerComponentsPage.clickOnLastDeleteButton();

    const deleteModal = element(by.className('modal'));
    await waitUntilDisplayed(deleteModal);

    timerDeleteDialog = new TimerDeleteDialog();
    expect(await timerDeleteDialog.getDialogTitle().getAttribute('id')).to.match(/rcraspiApp.timer.delete.question/);
    await timerDeleteDialog.clickOnConfirmButton();

    await timerComponentsPage.waitUntilDeleteButtonsLength(nbButtonsBeforeDelete - 1);
    expect(await timerComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
