import { combineReducers } from 'redux';
import { loadingBarReducer as loadingBar } from 'react-redux-loading-bar';

import authentication, { AuthenticationState } from './authentication';
import applicationProfile, { ApplicationProfileState } from './application-profile';

import administration, { AdministrationState } from 'app/modules/administration/administration.reducer';
import userManagement, { UserManagementState } from 'app/modules/administration/user-management/user-management.reducer';
import register, { RegisterState } from 'app/modules/account/register/register.reducer';
import activate, { ActivateState } from 'app/modules/account/activate/activate.reducer';
import password, { PasswordState } from 'app/modules/account/password/password.reducer';
import settings, { SettingsState } from 'app/modules/account/settings/settings.reducer';
import passwordReset, { PasswordResetState } from 'app/modules/account/password-reset/password-reset.reducer';
// prettier-ignore
import customer, {
  CustomerState
} from 'app/entities/customer.reducer';
import docGeneration, { DocGenerationState } from 'app/shared/reducers/doc-generation';
// prettier-ignore
import product, {
  ProductState
} from 'app/entities/product/product.reducer';
// prettier-ignore
import customerOrder, {
  CustomerOrderState
} from 'app/entities/customer-order/customer-order.reducer';
// prettier-ignore
import manualInvoice, {
  ManualInvoiceState
} from 'app/entities/manual-invoice/manual-invoice.reducer';
// prettier-ignore
// prettier-ignore
import manualLabel, {
  ManualLabelState
} from 'app/entities/manual-label/manual-label.reducer';
/* jhipster-needle-add-reducer-import - JHipster will add reducer here */

export interface IRootState {
  readonly authentication: AuthenticationState;
  readonly applicationProfile: ApplicationProfileState;
  readonly administration: AdministrationState;
  readonly userManagement: UserManagementState;
  readonly register: RegisterState;
  readonly activate: ActivateState;
  readonly passwordReset: PasswordResetState;
  readonly password: PasswordState;
  readonly settings: SettingsState;
  readonly customer: CustomerState;
  readonly product: ProductState;
  readonly customerOrder: CustomerOrderState;
  readonly manualInvoice: ManualInvoiceState;
  readonly manualLabel: ManualLabelState;
  /* jhipster-needle-add-reducer-type - JHipster will add reducer type here */
  readonly docGeneration: DocGenerationState;
  readonly loadingBar: any;
}

const rootReducer = combineReducers<IRootState>({
  authentication,
  applicationProfile,
  administration,
  userManagement,
  register,
  activate,
  passwordReset,
  password,
  settings,
  customer,
  product,
  customerOrder,
  manualInvoice,
  manualLabel,
  /* jhipster-needle-add-reducer-combine - JHipster will add reducer here */
  docGeneration,
  loadingBar
});

export default rootReducer;
