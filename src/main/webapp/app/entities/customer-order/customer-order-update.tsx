import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Col, Label, Row, Table } from 'reactstrap';
import { AvField, AvForm, AvGroup, AvInput } from 'availity-reactstrap-validation';
import { getEntities as getProducts } from 'app/entities/product/product.reducer';
// tslint:disable-next-line:no-unused-variable
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';
// todo this will need to be all types of customer, or not at all
import { getEntity as getCustomer } from 'app/entities/dampproofer/dampproofer.reducer';
import { addOrderItem, clearOrderItems, createEntity, getEntity, reset, updateEntity } from './customer-order.reducer';

// tslint:disable-next-line:no-unused-variable

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
    if (nextProps.updateSuccess !== this.props.updateSuccess && nextProps.updateSuccess && nextProps.customerOrderEntity.id) {
      this.handleClose(nextProps.customerOrderEntity.id);
    }
  }

  componentDidMount() {
    if (this.state.isNew) {
      this.props.reset();
      // todo this will need to be all types of customer, or not at all
      // todo this is horrible - improve
      const search = this.props.location.search;
      const n = search.lastIndexOf('customerId=');
      const result = search.substring(n + 11);
      this.props.getCustomer(result); // todo what if null?
    } else {
      this.props.getEntity(this.props.match.params.id);
    }

    this.props.getProducts();
  }

  saveEntity = (event, errors, values) => {
    if (errors.length === 0) {
      const { customerOrderEntity } = this.props;
      const entity = {
        customerId: this.props.customerEntity.id,
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

  handleClose = customerOrderId => {
    this.props.history.push('/entity/customer-order/' + customerOrderId);
  };

  render() {
    const { customerOrderEntity, customerEntity, loading, updating, products } = this.props;
    const { isNew } = this.state;

    const clearItems = () => {
      this.props.clearOrderItems();
    };
    const addItem = () => {
      this.props.addOrderItem();
    };

    // explicitly set null fields to empty string, otherwise causes problems with validation
    Object.keys(customerOrderEntity).forEach(key => {
      if (customerOrderEntity[key] == null) {
        customerOrderEntity[key] = '';
      }
    });

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
              // todo default vatRate and product to config?
              <AvForm model={isNew ? { items: [{ productId: 6 }], vatRate: 20 } : customerOrderEntity} onSubmit={this.saveEntity}>
                {!isNew ? (
                  <AvGroup>
                    <Label for="id">ID</Label>
                    <AvInput id="customer-order-id" type="text" className="form-control" name="id" required readOnly />
                  </AvGroup>
                ) : null}
                {!isNew ? (
                  <AvGroup>
                    <Label for="customerName">Customer</Label>
                    <AvInput id="customer-name" type="text" className="form-control" name="customerName" required readOnly />
                    <AvInput id="customer-id" type="text" className="form-control" name="customerId" required readOnly hidden />
                  </AvGroup>
                ) : (
                  <AvGroup>
                    <Label for="customerName">Customer</Label>
                    <input id="customer-name" type="text" className="form-control" value={customerEntity.companyName} required readOnly />
                    <input
                      id="customer-id"
                      type="text"
                      className="form-control"
                      value={customerEntity.customerId}
                      required
                      readOnly
                      hidden
                    />
                  </AvGroup>
                )}
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
                <div className="table-responsive table-sm">
                  <Table responsive className="table-bordered">
                    <thead className="thead-light">
                      <tr>
                        <th className="hand">ID</th>
                        <th className="hand">Product</th>
                        <th className="hand">Quantity</th>
                        <th className="hand">Price</th>
                        <th className="hand">notes</th>
                        <th className="hand">serial number</th>
                      </tr>
                    </thead>
                    <tbody>
                      {customerOrderEntity.items
                        ? customerOrderEntity.items.map((item, i) => (
                            <tr key={`entity-${i}`}>
                              <td>
                                <AvField id={'customer-order-item-id-' + i} type="text" name={'items[' + i + '].id'} disabled />
                              </td>
                              <td>
                                <AvGroup>
                                  <AvInput
                                    id={'customer-order-item-product-' + i}
                                    type="select"
                                    className="form-control"
                                    name={'items[' + i + '].productId'}
                                  >
                                    {products
                                      ? products.map(otherEntity => (
                                          <option value={otherEntity.id} key={otherEntity.id}>
                                            {otherEntity.name}
                                          </option>
                                        ))
                                      : null}
                                  </AvInput>
                                </AvGroup>
                              </td>
                              <td>
                                <AvField
                                  id={'customer-order-item-qty-' + i}
                                  type="text"
                                  name={'items[' + i + '].quantity'}
                                  validate={{
                                    required: { value: true, errorMessage: 'This field is required.' }
                                  }}
                                />
                              </td>
                              <td>
                                <AvField
                                  id={'customer-order-item-price-' + i}
                                  type="text"
                                  name={'items[' + i + '].price'}
                                  validate={{
                                    required: { value: true, errorMessage: 'This field is required.' }
                                  }}
                                />
                              </td>
                              <td>
                                <AvField id={'customer-order-item-notes-' + i} type="text" name={'items[' + i + '].notes'} />
                              </td>
                              <td>
                                <AvField id={'customer-order-item-serialNumber-' + i} type="text" name={'items[' + i + '].serialNumber'} />
                              </td>
                            </tr>
                          ))
                        : null}
                    </tbody>
                  </Table>
                  <Button id="clearItems" onClick={clearItems} replace color="danger">
                    <FontAwesomeIcon icon="trash" />
                    clear
                  </Button>
                  <Button id="addItem" onClick={addItem} replace color="info">
                    <FontAwesomeIcon icon="plus" />
                    add
                  </Button>
                </div>
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
  products: storeState.product.entities,
  customerEntity: storeState.customer.entity,
  customerOrderEntity: storeState.customerOrder.entity,
  loading: storeState.customerOrder.loading,
  updating: storeState.customerOrder.updating,
  updateSuccess: storeState.customerOrder.updateSuccess
});

const mapDispatchToProps = {
  getProducts,
  getCustomer,
  getEntity,
  updateEntity,
  createEntity,
  reset,
  clearOrderItems,
  addOrderItem
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(CustomerOrderUpdate);
