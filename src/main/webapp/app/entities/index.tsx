import React from 'react';
import { Switch } from 'react-router-dom';

// tslint:disable-next-line:no-unused-variable
import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Controller from './controller';
import Pin from './pin';
import Temperature from './temperature';
import Humidity from './humidity';
import Sensor from './sensor';
/* jhipster-needle-add-route-import - JHipster will add routes here */

const Routes = ({ match }) => (
  <div>
    <Switch>
      {/* prettier-ignore */}
      <ErrorBoundaryRoute path={`${match.url}/controller`} component={Controller} />
      <ErrorBoundaryRoute path={`${match.url}/pin`} component={Pin} />
      <ErrorBoundaryRoute path={`${match.url}/temperature`} component={Temperature} />
      <ErrorBoundaryRoute path={`${match.url}/humidity`} component={Humidity} />
      <ErrorBoundaryRoute path={`${match.url}/sensor`} component={Sensor} />
      {/* jhipster-needle-add-route-path - JHipster will routes here */}
    </Switch>
  </div>
);

export default Routes;
