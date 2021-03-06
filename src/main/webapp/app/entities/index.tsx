import React from 'react';
import { Switch } from 'react-router-dom';

// tslint:disable-next-line:no-unused-variable
import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Dampproofer from './dampproofer';
import Domestic from './domestic';
import Product from './product';
import CustomerOrder from './customer-order';
import ManualInvoice from './manual-invoice';
import ManualLabel from './manual-label';
/* jhipster-needle-add-route-import - JHipster will add routes here */

const Routes = ({ match }) => (
  <div>
    <Switch>
      {/* prettier-ignore */}
      <ErrorBoundaryRoute path={`${match.url}/dampproofer`} component={Dampproofer} />
      <ErrorBoundaryRoute path={`${match.url}/domestic`} component={Domestic} />
      <ErrorBoundaryRoute path={`${match.url}/product`} component={Product} />
      <ErrorBoundaryRoute path={`${match.url}/customer-order`} component={CustomerOrder} />
      <ErrorBoundaryRoute path={`${match.url}/manual-invoice`} component={ManualInvoice} />
      <ErrorBoundaryRoute path={`${match.url}/manual-label`} component={ManualLabel} />
      {/* jhipster-needle-add-route-path - JHipster will routes here */}
    </Switch>
  </div>
);

export default Routes;
