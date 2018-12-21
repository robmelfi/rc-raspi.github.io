/* tslint:disable no-unused-expression */
import { browser, element, by } from 'protractor';

import NavBarPage from './../../page-objects/navbar-page';
import SignInPage from './../../page-objects/signin-page';
import SensorComponentsPage from './sensor.page-object';
import { SensorDeleteDialog } from './sensor.page-object';
import SensorUpdatePage from './sensor-update.page-object';
import { waitUntilDisplayed, waitUntilHidden } from '../../util/utils';

const expect = chai.expect;

describe('Sensor e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let sensorUpdatePage: SensorUpdatePage;
  let sensorComponentsPage: SensorComponentsPage;
  let sensorDeleteDialog: SensorDeleteDialog;

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

  it('should load Sensors', async () => {
    await navBarPage.getEntityPage('sensor');
    sensorComponentsPage = new SensorComponentsPage();
    expect(await sensorComponentsPage.getTitle().getText()).to.match(/Sensors/);
  });

  it('should load create Sensor page', async () => {
    await sensorComponentsPage.clickOnCreateButton();
    sensorUpdatePage = new SensorUpdatePage();
    expect(await sensorUpdatePage.getPageTitle().getAttribute('id')).to.match(/rcraspiApp.sensor.home.createOrEditLabel/);
  });

  it('should create and save Sensors', async () => {
    const nbButtonsBeforeCreate = await sensorComponentsPage.countDeleteButtons();

    await sensorUpdatePage.setNameInput('name');
    expect(await sensorUpdatePage.getNameInput()).to.match(/name/);
    await sensorUpdatePage.setDescriptionInput('description');
    expect(await sensorUpdatePage.getDescriptionInput()).to.match(/description/);
    await sensorUpdatePage.setImagePathInput('imagePath');
    expect(await sensorUpdatePage.getImagePathInput()).to.match(/imagePath/);
    await waitUntilDisplayed(sensorUpdatePage.getSaveButton());
    await sensorUpdatePage.save();
    await waitUntilHidden(sensorUpdatePage.getSaveButton());
    expect(await sensorUpdatePage.getSaveButton().isPresent()).to.be.false;

    await sensorComponentsPage.waitUntilDeleteButtonsLength(nbButtonsBeforeCreate + 1);
    expect(await sensorComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1);
  });

  it('should delete last Sensor', async () => {
    await sensorComponentsPage.waitUntilLoaded();
    const nbButtonsBeforeDelete = await sensorComponentsPage.countDeleteButtons();
    await sensorComponentsPage.clickOnLastDeleteButton();

    const deleteModal = element(by.className('modal'));
    await waitUntilDisplayed(deleteModal);

    sensorDeleteDialog = new SensorDeleteDialog();
    expect(await sensorDeleteDialog.getDialogTitle().getAttribute('id')).to.match(/rcraspiApp.sensor.delete.question/);
    await sensorDeleteDialog.clickOnConfirmButton();

    await sensorComponentsPage.waitUntilDeleteButtonsLength(nbButtonsBeforeDelete - 1);
    expect(await sensorComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
