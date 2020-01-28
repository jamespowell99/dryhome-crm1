import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Col, Label, Row, Table } from 'reactstrap';
import { AvField, AvForm, AvGroup, AvInput } from 'availity-reactstrap-validation';
// tslint:disable-next-line:no-unused-variable
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import {
  addInvoiceItem,
  clearInvoiceItems,
  createEntity,
  getEntity,
  itemPriceChanged,
  itemQtyChanged,
  paymentDateChanged,
  reset,
  updateEntity,
  vatRateChanged
} from './manual-invoice.reducer';

// tslint:disable-next-line:no-unused-variable

export interface IManualInvoiceUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export interface IManualInvoiceUpdateState {
  isNew: boolean;
}

export class ManualInvoiceUpdate extends React.Component<IManualInvoiceUpdateProps, IManualInvoiceUpdateState> {
  constructor(props) {
    super(props);
    this.state = {
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
  }

  saveEntity = (event, errors, values) => {
    if (errors.length === 0) {
      const { manualInvoiceEntity } = this.props;
      const entity = {
        ...manualInvoiceEntity,
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
    this.props.history.push('/entity/manual-invoice');
  };

  render() {
    const { manualInvoiceEntity, loading, updating } = this.props;
    const { isNew } = this.state;

    const clearItem = i => {
      this.props.clearInvoiceItems(i);
    };
    const addItem = () => {
      this.props.addInvoiceItem();
    };

    const handlePaymentDateChange = e => {
      this.props.paymentDateChanged(e.target.value);
    };

    const handleItemPriceChange = e => {
      this.props.itemPriceChanged(+e.target.id.split('-').pop(), e.target.value);
    };

    const handleItemQtyChange = e => {
      this.props.itemQtyChanged(+e.target.id.split('-').pop(), e.target.value);
    };

    const handleVatRateChange = e => {
      this.props.vatRateChanged(e.target.value);
    };

    // explicitly set null fields to empty string, otherwise causes problems with validation
    Object.keys(manualInvoiceEntity).forEach(key => {
      if (manualInvoiceEntity[key] == null) {
        manualInvoiceEntity[key] = '';
      }
    });

    return (
      <div>
        <Row className="justify-content-center">
          <Col md="8">
            <h2 id="dryhomecrm1App.manualInvoice.home.createOrEditLabel">Create or edit a ManualInvoice</h2>
          </Col>
        </Row>
        <Row className="justify-content-center">
          <Col>
            {loading ? (
              <p>Loading...</p>
            ) : (
              <AvForm model={isNew ? { items: [{}], vatRate: 20 } : manualInvoiceEntity} onSubmit={this.saveEntity}>
                {/*// todo default vatRate to config?*/}
                <Row>
                  <Col className="border mx-1">
                    <span>Order Details</span>
                    {!isNew ? (
                      <AvGroup>
                        <Label for="id">ID</Label>
                        <AvInput id="manual-invoice-id" type="text" className="form-control" name="id" required readOnly />
                      </AvGroup>
                    ) : null}
                    <AvGroup>
                      <Label id="invoiceNumberLabel" for="invoiceNumber">
                        Invoice Number
                      </Label>
                      <AvField
                        id="manual-invoice-invoiceNumber"
                        type="text"
                        name="invoiceNumber"
                        validate={{
                          required: { value: true, errorMessage: 'This field is required.' }
                        }}
                      />
                    </AvGroup>
                    <AvGroup>
                      <Label id="orderNumberLabel" for="orderNumber">
                        Order Number
                      </Label>
                      <AvField
                        id="manual-invoice-orderNumber"
                        type="text"
                        name="orderNumber"
                        validate={{
                          required: { value: true, errorMessage: 'This field is required.' }
                        }}
                      />
                    </AvGroup>
                    <AvGroup>
                      <Label id="invoiceDateLabel" for="invoiceDate">
                        Invoice Date
                      </Label>
                      <AvField
                        id="manual-invoice-invoiceDate"
                        type="date"
                        className="form-control"
                        name="invoiceDate"
                        validate={{
                          required: { value: true, errorMessage: 'This field is required.' }
                        }}
                      />
                    </AvGroup>
                    <AvGroup>
                      <Label id="refLabel" for="ref">
                        Ref
                      </Label>
                      <AvField id="manual-invoice-ref" type="text" name="ref" />
                    </AvGroup>
                    <AvGroup>
                      <Label id="customerLabel" for="customer">
                        Customer
                      </Label>
                      <AvField id="manual-invoice-customer" type="text" name="customer" />
                    </AvGroup>
                    <AvGroup>
                      <Label id="specialInstructions1Label" for="specialInstructions1">
                        Special Instructions 1
                      </Label>
                      <AvField id="manual-invoice-specialInstructions1" type="text" name="specialInstructions1" />
                    </AvGroup>
                    <AvGroup>
                      <Label id="specialInstructions2Label" for="specialInstructions2">
                        Special Instructions 2
                      </Label>
                      <AvField id="manual-invoice-specialInstructions2" type="text" name="specialInstructions2" />
                    </AvGroup>
                  </Col>
                  <Col className="border mx-1">
                    <span>Invoice Contact / Address</span>
                    <AvGroup>
                      <Label id="address1Label" for="address1">
                        Address 1
                      </Label>
                      <AvField id="manual-invoice-address1" type="text" name="address1" />
                    </AvGroup>
                    <AvGroup>
                      <Label id="address2Label" for="address2">
                        Address 2
                      </Label>
                      <AvField id="manual-invoice-address2" type="text" name="address2" />
                    </AvGroup>
                    <AvGroup>
                      <Label id="address3Label" for="address3">
                        Address 3
                      </Label>
                      <AvField id="manual-invoice-address3" type="text" name="address3" />
                    </AvGroup>
                    <AvGroup>
                      <Label id="townLabel" for="town">
                        Town
                      </Label>
                      <AvField id="manual-invoice-town" type="text" name="town" />
                    </AvGroup>
                    <AvGroup>
                      <Label id="postCodeLabel" for="postCode">
                        Post Code
                      </Label>
                      <AvField id="manual-invoice-postCode" type="text" name="postCode" />
                    </AvGroup>
                    <AvGroup>
                      <Label id="telNoLabel" for="telNo">
                        Tel No
                      </Label>
                      <AvField id="manual-invoice-telNo" type="text" name="telNo" />
                    </AvGroup>
                  </Col>
                  <Col className="border mx-1">
                    <span>Delivery Contact / Address</span>
                    <AvGroup>
                      <Label id="deliveryAddress1Label" for="deliveryAddress1">
                        Delivery Address 1
                      </Label>
                      <AvField id="manual-invoice-deliveryAddress1" type="text" name="deliveryAddress1" />
                    </AvGroup>
                    <AvGroup>
                      <Label id="deliveryAddress2Label" for="deliveryAddress2">
                        Delivery Address 2
                      </Label>
                      <AvField id="manual-invoice-deliveryAddress2" type="text" name="deliveryAddress2" />
                    </AvGroup>
                    <AvGroup>
                      <Label id="deliveryAddress3Label" for="deliveryAddress3">
                        Delivery Address 3
                      </Label>
                      <AvField id="manual-invoice-deliveryAddress3" type="text" name="deliveryAddress3" />
                    </AvGroup>
                    <AvGroup>
                      <Label id="deliveryAddress4Label" for="deliveryAddress4">
                        Delivery Address 4
                      </Label>
                      <AvField id="manual-invoice-deliveryAddress4" type="text" name="deliveryAddress4" />
                    </AvGroup>
                  </Col>
                </Row>
                <Row>
                  <Col>
                    <div className="table-responsive table-sm m-2">
                      <b>Order Items</b>
                      <Table responsive className="table-bordered">
                        <thead className="thead-light">
                          <tr>
                            <th className="hand">Product</th>
                            <th className="hand">Quantity</th>
                            <th className="hand">Price</th>
                            <th />
                          </tr>
                        </thead>
                        <tbody>
                          {manualInvoiceEntity.items
                            ? manualInvoiceEntity.items.map((item, i) => (
                                <tr key={`entity-${i}`}>
                                  <td>
                                    <AvGroup>
                                      <AvField
                                        id={'manual-invoice-item-product-' + i}
                                        type="text"
                                        name={'items[' + i + '].product'}
                                        validate={{
                                          required: { value: true, errorMessage: 'This field is required.' }
                                        }}
                                        value={manualInvoiceEntity.items[i].product || ''}
                                      />
                                    </AvGroup>
                                  </td>
                                  <td>
                                    <AvField
                                      id={'manual-invoice-item-qty-' + i}
                                      type="text"
                                      name={'items[' + i + '].quantity'}
                                      onChange={handleItemQtyChange}
                                      value={manualInvoiceEntity.items[i].quantity || (isNew && i === 0 ? 0 : '')}
                                      validate={{
                                        required: { value: true, errorMessage: 'This field is required.' }
                                      }}
                                    />
                                  </td>
                                  <td>
                                    <AvField
                                      id={'manual-invoice-item-price-' + i}
                                      type="text"
                                      name={'items[' + i + '].price'}
                                      value={manualInvoiceEntity.items[i].price || (isNew && i === 0 ? 0 : '')}
                                      onChange={handleItemPriceChange}
                                      validate={{
                                        required: { value: true, errorMessage: 'This field is required.' }
                                      }}
                                    />
                                  </td>
                                  <td>
                                    {manualInvoiceEntity.items && manualInvoiceEntity.items.length > 1 ? (
                                      <Button color="danger" size="sm" onClick={() => clearItem(i)} id={'delete-item-' + i}>
                                        <FontAwesomeIcon icon="trash" />
                                      </Button>
                                    ) : (
                                      <div />
                                    )}
                                  </td>
                                  <AvField id={'manual-invoice-item-id-' + i} type="text" name={'items[' + i + '].id'} disabled hidden />
                                </tr>
                              ))
                            : null}
                        </tbody>
                      </Table>
                      <Button id="clearItems" onClick={clearItem} replace color="danger">
                        <FontAwesomeIcon icon="trash" />
                        clear
                      </Button>
                      <Button id="addItem" onClick={addItem} replace color="info">
                        <FontAwesomeIcon icon="plus" />
                        add
                      </Button>
                    </div>
                  </Col>
                </Row>
                <Row>
                  <Col className="border mx-1" />
                  <Col className="border mx-1">
                    <b>Payment Details</b>
                    <AvGroup>
                      <Label id="paymentDateLabel" for="paymentDate">
                        Payment Date
                      </Label>
                      <AvField
                        id="manual-invoice-paymentDate"
                        type="date"
                        className="form-control"
                        name="paymentDate"
                        onChange={handlePaymentDateChange}
                      />
                    </AvGroup>
                    <AvGroup>
                      <Label id="paymentStatusLabel" for="paymentStatus">
                        Payment Status
                      </Label>
                      <AvField
                        id="manual-invoice-paymentStatus"
                        type="text"
                        name="paymentStatus"
                        disabled={!manualInvoiceEntity.paymentDate}
                        value={manualInvoiceEntity.paymentStatus || ''}
                      />
                    </AvGroup>
                    <AvGroup>
                      <Label id="paymentTypeLabel" for="paymentType">
                        Payment Type
                      </Label>
                      <AvField
                        id="manual-invoice-paymentType"
                        type="text"
                        name="paymentType"
                        disabled={!manualInvoiceEntity.paymentDate}
                        value={manualInvoiceEntity.paymentType || ''}
                      />
                    </AvGroup>
                    <AvGroup>
                      <Label id="paymentAmountLabel" for="paymentAmount">
                        Payment Amount
                      </Label>
                      <AvField
                        id="manual-invoice-paymentAmount"
                        type="text"
                        name="paymentAmount"
                        disabled={!manualInvoiceEntity.paymentDate}
                        value={manualInvoiceEntity.paymentAmount || ''}
                      />
                    </AvGroup>
                  </Col>
                  <Col className="border mx-1">
                    <b>Totals</b>
                    <AvGroup>
                      <Label id="vatRateLabel" for="vatRate">
                        Vat Rate
                      </Label>
                      <AvField
                        id="manual-invoice-vatRate"
                        type="text"
                        name="vatRate"
                        onChange={handleVatRateChange}
                        validate={{
                          required: { value: true, errorMessage: 'This field is required.' },
                          number: { value: true, errorMessage: 'This field should be a number.' }
                        }}
                      />
                    </AvGroup>
                    <AvGroup>
                      <Label id="subTotalLabel" for="subTotal">
                        Sub Total
                      </Label>
                      <AvField
                        id="manual-invoice-subTotal"
                        type="text"
                        name="calculatedSubTotal"
                        value={this.props.subTotal ? this.props.subTotal.toFixed(2) : 0}
                        disabled
                      />
                      <AvGroup>
                        <Label id="vatAmountLabel" for="calculatedVatAmount">
                          VAT
                        </Label>
                        <AvField
                          id="manual-invoice-vatAmount"
                          type="text"
                          name="calculatedVatAmount"
                          value={this.props.vatAmount ? this.props.vatAmount.toFixed(2) : 0}
                          disabled
                        />
                      </AvGroup>
                      <AvGroup>
                        <Label id="totalLabel" for="calculatedTotal">
                          Total
                        </Label>
                        <AvField
                          id="manual-invoice-total"
                          type="text"
                          name="calculatedTotal"
                          value={this.props.total ? this.props.total.toFixed(2) : 0}
                          disabled
                        />
                      </AvGroup>
                    </AvGroup>
                  </Col>
                </Row>
                <Button tag={Link} id="cancel-save" to="/entity/manual-invoice" replace color="info">
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
  manualInvoiceEntity: storeState.manualInvoice.entity,
  loading: storeState.manualInvoice.loading,
  updating: storeState.manualInvoice.updating,
  updateSuccess: storeState.manualInvoice.updateSuccess,
  subTotal: storeState.manualInvoice.subTotal,
  vatAmount: storeState.manualInvoice.vatAmount,
  total: storeState.manualInvoice.total
});

const mapDispatchToProps = {
  getEntity,
  updateEntity,
  createEntity,
  reset,
  clearInvoiceItems,
  addInvoiceItem,
  paymentDateChanged,
  itemPriceChanged,
  itemQtyChanged,
  vatRateChanged
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(ManualInvoiceUpdate);
