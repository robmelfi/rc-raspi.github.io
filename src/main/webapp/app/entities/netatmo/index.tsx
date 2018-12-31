import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Netatmo from './netatmo';
import NetatmoDetail from './netatmo-detail';
import NetatmoUpdate from './netatmo-update';
import NetatmoDeleteDialog from './netatmo-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={NetatmoUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={NetatmoUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={NetatmoDetail} />
      <ErrorBoundaryRoute path={match.url} component={Netatmo} />
    </Switch>
    <ErrorBoundaryRoute path={`${match.url}/:id/delete`} component={NetatmoDeleteDialog} />
  </>
);

export default Routes;
