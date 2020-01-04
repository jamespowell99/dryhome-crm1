import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
// tslint:disable-next-line:no-unused-variable
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { ICustomer } from 'app/shared/model/customer.model';
// todo this will need to be all types of customer, or not at all
import { getEntities as getCustomers } from 'app/entities/dampproofer/dampproofer.reducer';
import { getEntity, updateEntity, createEntity, reset } from './customer-order.reducer';
import { ICustomerOrder } from 'app/shared/model/customer-order.model';
// tslint:disable-next-line:no-unused-variable
import { convertDateTimeFromServer, convertDateTimeToServer } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface ICustomerOrderUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export interface ICustomerOrderUpdateState {
  isNew: boolean;
  customerId: string;
}

export class CustomerOrderUpdate extends React.Component<ICustomerOrderUpdateProps, ICustomerOrderUpdateState> {
  constructor(props) {
    super(props);
    this.state = {
      customerId: '0',
      isNew: !this.props.match.params || !this.props.match.params.id
    };
  }

  componentWillUpdate(nextProps, nextState) {
    if (nextProps.updateSuccess !== this.props.updateSuccess && nextProps.updateSuccess) {
      this.handleClose();
    }
  }

  componentDidMount() {
    if (this.state.isNew) {
      this.props.reset();
    } else {
      this.props.getEntity(this.props.match.params.id);
    }

    // todo this will need to be all types of customer, or not at all
    this.props.getCustomers('DAMP_PROOFER');
  }

  saveEntity = (event, errors, values) => {
    if (errors.length === 0) {
      const { customerOrderEntity } = this.props;
      const entity = {
        ...customerOrderEntity,
        ...values
      };

      if (this.state.isNew) {
        this.props.createEntity(entity);
      } else {
        this.props.updateEntity(entity);
      }
    }
  };

  handleClose = () => {
    this.props.history.push('/entity/customer-order');
  };

  render() {
    const { customerOrderEntity, customers, loading, updating } = this.props;
    const { isNew } = this.state;

    return (
      <div>
        <Row className="justify-content-center">
          <Col md="8">
            <h2 id="dryhomecrm1App.customerOrder.home.createOrEditLabel">Create or edit a CustomerOrder</h2>
          </Col>
        </Row>
        <Row className="justify-content-center">
          <Col md="8">
            {loading ? (
              <p>Loading...</p>
            ) : (
              <AvForm model={isNew ? {} : customerOrderEntity} onSubmit={this.saveEntity}>
                {!isNew ? (
                  <AvGroup>
                    <Label for="id">ID</Label>
                    <AvInput id="customer-order-id" type="text" className="form-control" name="id" required readOnly />
                  </AvGroup>
                ) : null}
                <AvGroup>
                  <Label id="orderNumberLabel" for="orderNumber">
                    Order Number
                  </Label>
                  <AvField
                    id="customer-order-orderNumber"
                    type="text"
                    name="orderNumber"
                    validate={{
                      required: { value: true, errorMessage: 'This field is required.' },
                      maxLength: { value: 10, errorMessage: 'This field cannot be longer than 10 characters.' }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="orderDateLabel" for="orderDate">
                    Order Date
                  </Label>
                  <AvField
                    id="customer-order-orderDate"
                    type="date"
                    className="form-control"
                    name="orderDate"
                    validate={{
                      required: { value: true, errorMessage: 'This field is required.' }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="notes1Label" for="notes1">
                    Notes 1
                  </Label>
                  <AvField
                    id="customer-order-notes1"
                    type="text"
                    name="notes1"
                    validate={{
                      maxLength: { value: 100, errorMessage: 'This field cannot be longer than 100 characters.' }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="notes2Label" for="notes2">
                    Notes 2
                  </Label>
                  <AvField
                    id="customer-order-notes2"
                    type="text"
                    name="notes2"
                    validate={{
                      maxLength: { value: 100, errorMessage: 'This field cannot be longer than 100 characters.' }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="despatchDateLabel" for="despatchDate">
                    Despatch Date
                  </Label>
                  <AvField id="customer-order-despatchDate" type="date" className="form-control" name="despatchDate" />
                </AvGroup>
                <AvGroup>
                  <Label id="invoiceDateLabel" for="invoiceDate">
                    Invoice Date
                  </Label>
                  <AvField id="customer-order-invoiceDate" type="date" className="form-control" name="invoiceDate" />
                </AvGroup>
                <AvGroup>
                  <Label id="paymentDateLabel" for="paymentDate">
                    Payment Date
                  </Label>
                  <AvField id="customer-order-paymentDate" type="date" className="form-control" name="paymentDate" />
                </AvGroup>
                <AvGroup>
                  <Label id="vatRateLabel" for="vatRate">
                    Vat Rate
                  </Label>
                  <AvField
                    id="customer-order-vatRate"
                    type="text"
                    name="vatRate"
                    validate={{
                      required: { value: true, errorMessage: 'This field is required.' },
                      number: { value: true, errorMessage: 'This field should be a number.' }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="internalNotesLabel" for="internalNotes">
                    Internal Notes
                  </Label>
                  <AvField id="customer-order-internalNotes" type="text" name="internalNotes" />
                </AvGroup>
                <AvGroup>
                  <Label id="invoiceNumberLabel" for="invoiceNumber">
                    Invoice Number
                  </Label>
                  <AvField
                    id="customer-order-invoiceNumber"
                    type="text"
                    name="invoiceNumber"
                    validate={{
                      maxLength: { value: 10, errorMessage: 'This field cannot be longer than 10 characters.' }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="paymentStatusLabel" for="paymentStatus">
                    Payment Status
                  </Label>
                  <AvField
                    id="customer-order-paymentStatus"
                    type="text"
                    name="paymentStatus"
                    validate={{
                      maxLength: { value: 100, errorMessage: 'This field cannot be longer than 100 characters.' }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="paymentTypeLabel" for="paymentType">
                    Payment Type
                  </Label>
                  <AvField
                    id="customer-order-paymentType"
                    type="text"
                    name="paymentType"
                    validate={{
                      maxLength: { value: 100, errorMessage: 'This field cannot be longer than 100 characters.' }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="paymentAmountLabel" for="paymentAmount">
                    Payment Amount
                  </Label>
                  <AvField id="customer-order-paymentAmount" type="text" name="paymentAmount" />
                </AvGroup>
                <AvGroup>
                  <Label id="placedByLabel" for="placedBy">
                    Placed By
                  </Label>
                  <AvField
                    id="customer-order-placedBy"
                    type="text"
                    name="placedBy"
                    validate={{
                      maxLength: { value: 100, errorMessage: 'This field cannot be longer than 100 characters.' }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="methodLabel">Method</Label>
                  <AvInput
                    id="customer-order-method"
                    type="select"
                    className="form-control"
                    name="method"
                    value={(!isNew && customerOrderEntity.method) || 'PHONE'}
                  >
                    <option value="PHONE">PHONE</option>
                    <option value="FAX">FAX</option>
                    <option value="EMAIL">EMAIL</option>
                    <option value="IN_PERSON">IN_PERSON</option>
                  </AvInput>
                </AvGroup>
                <AvGroup>
                  <Label for="customer.id">Customer</Label>
                  <AvInput id="customer-order-customer" type="select" className="form-control" name="customerId">
                    {customers
                      ? customers.map(otherEntity => (
                          <option value={otherEntity.id} key={otherEntity.id}>
                            {otherEntity.id}
                          </option>
                        ))
                      : null}
                  </AvInput>
                </AvGroup>
                <Button tag={Link} id="cancel-save" to="/entity/customer-order" replace color="info">
                  <FontAwesomeIcon icon="arrow-left" />
                  &nbsp;
                  <span className="d-none d-md-inline">Back</span>
                </Button>
                &nbsp;
                <Button color="primary" id="save-entity" type="submit" disabled={updating}>
                  <FontAwesomeIcon icon="save" />
                  &nbsp; Save
                </Button>
              </AvForm>
            )}
          </Col>
        </Row>
      </div>
    );
  }
}

const mapStateToProps = (storeState: IRootState) => ({
  customers: storeState.customer.entities,
  customerOrderEntity: storeState.customerOrder.entity,
  loading: storeState.customerOrder.loading,
  updating: storeState.customerOrder.updating,
  updateSuccess: storeState.customerOrder.updateSuccess
});

const mapDispatchToProps = {
  getCustomers,
  getEntity,
  updateEntity,
  createEntity,
  reset
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(CustomerOrderUpdate);
