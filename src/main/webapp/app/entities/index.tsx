import React from 'react';
import { Switch } from 'react-router-dom';

// tslint:disable-next-line:no-unused-variable
import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Dampproofer from './dampproofer';
import Domestic from './domestic';
import Product from './product';
/* jhipster-needle-add-route-import - JHipster will add routes here */

const Routes = ({ match }) => (
  <div>
    <Switch>
      {/* prettier-ignore */}
      <ErrorBoundaryRoute path={`${match.url}/dampproofer`} component={Dampproofer} />
      <ErrorBoundaryRoute path={`${match.url}/domestic`} component={Domestic} />
      <ErrorBoundaryRoute path={`${match.url}/product`} component={Product} />
      {/* jhipster-needle-add-route-path - JHipster will routes here */}
    </Switch>
  </div>
);

export default Routes;
