import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Pin from './pin';
import PinDetail from './pin-detail';
import PinUpdate from './pin-update';
import PinDeleteDialog from './pin-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={PinUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={PinUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={PinDetail} />
      <ErrorBoundaryRoute path={match.url} component={Pin} />
    </Switch>
    <ErrorBoundaryRoute path={`${match.url}/:id/delete`} component={PinDeleteDialog} />
  </>
);

export default Routes;
