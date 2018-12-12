import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Sensor from './sensor';
import SensorDetail from './sensor-detail';
import SensorUpdate from './sensor-update';
import SensorDeleteDialog from './sensor-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={SensorUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={SensorUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={SensorDetail} />
      <ErrorBoundaryRoute path={match.url} component={Sensor} />
    </Switch>
    <ErrorBoundaryRoute path={`${match.url}/:id/delete`} component={SensorDeleteDialog} />
  </>
);

export default Routes;
