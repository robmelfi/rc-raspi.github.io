import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Controller from './controller';
import ControllerDetail from './controller-detail';
import ControllerUpdate from './controller-update';
import ControllerDeleteDialog from './controller-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={ControllerUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={ControllerUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={ControllerDetail} />
      <ErrorBoundaryRoute path={match.url} component={Controller} />
    </Switch>
    <ErrorBoundaryRoute path={`${match.url}/:id/delete`} component={ControllerDeleteDialog} />
  </>
);

export default Routes;
