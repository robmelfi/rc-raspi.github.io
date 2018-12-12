import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Humidity from './humidity';
import HumidityDetail from './humidity-detail';
import HumidityUpdate from './humidity-update';
import HumidityDeleteDialog from './humidity-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={HumidityUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={HumidityUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={HumidityDetail} />
      <ErrorBoundaryRoute path={match.url} component={Humidity} />
    </Switch>
    <ErrorBoundaryRoute path={`${match.url}/:id/delete`} component={HumidityDeleteDialog} />
  </>
);

export default Routes;
