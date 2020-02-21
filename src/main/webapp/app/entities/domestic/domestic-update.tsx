import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
// tslint:disable-next-line:no-unused-variable
import { ICrudGetAction, ICrudGetAllAction, setFileData, byteSize, ICrudPutAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { getEntity, updateEntity, createEntity, setBlob, reset } from 'app/entities/domestic/domestic.reducer';
import { ICustomer } from 'app/shared/model/customer.model';
// tslint:disable-next-line:no-unused-variable
import { convertDateTimeFromServer, convertDateTimeToServer } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface ICustomerUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export interface ICustomerUpdateState {
  isNew: boolean;
}

export class CustomerUpdate extends React.Component<ICustomerUpdateProps, ICustomerUpdateState> {
  constructor(props) {
    super(props);
    this.state = {
      isNew: !this.props.match.params || !this.props.match.params.id
    };
  }

  componentWillUpdate(nextProps, nextState) {
    if (nextProps.updateSuccess !== this.props.updateSuccess && nextProps.updateSuccess && nextProps.customerEntity.id) {
      this.handleClose(nextProps.customerEntity.id);
    }
  }

  componentDidMount() {
    if (this.state.isNew) {
      this.props.reset();
    } else {
      this.props.getEntity(this.props.match.params.id);
    }
  }

  onBlobChange = (isAnImage, name) => event => {
    setFileData(event, (contentType, data) => this.props.setBlob(name, data, contentType), isAnImage);
  };

  clearBlob = name => () => {
    this.props.setBlob(name, undefined, undefined);
  };

  saveEntity = (event, errors, values) => {
    if (errors.length === 0) {
      const { customerEntity } = this.props;
      const entity = {
        ...customerEntity,
        ...values
      };

      if (this.state.isNew) {
        this.props.createEntity(entity);
      } else {
        this.props.updateEntity(entity);
      }
    }
  };

  handleClose = customerId => {
    this.props.history.push('/entity/domestic/' + customerId);
  };

  render() {
    const { customerEntity, loading, updating } = this.props;
    const { isNew } = this.state;

    const { notes } = customerEntity;

    // explicitly set null fields to empty string, otherwise causes problems with validation
    Object.keys(customerEntity).forEach(key => {
      if (customerEntity[key] == null) {
        customerEntity[key] = '';
      }
    });

    return (
      <div>
        <Row>
          <Col>
            <h2 id="dryhomecrm1App.customer.home.createOrEditLabel">Create or edit a Domestic</h2>
          </Col>
        </Row>
        {loading ? (
          <p>Loading...</p>
        ) : (
          <AvForm model={isNew ? {} : customerEntity} onSubmit={this.saveEntity}>
            <Row>
              <Col>
                {!isNew ? (
                  <AvGroup>
                    <Label for="id">ID</Label>
                    <AvInput id="customer-id" type="text" className="form-control" name="id" required readOnly />
                  </AvGroup>
                ) : null}
                <AvGroup>
                  <AvInput id="customer-type" type="text" hidden className="form-control" name="type" value="DOMESTIC" />
                </AvGroup>
                <AvGroup>
                  <Label id="address1Label" for="address1">
                    Address 1
                  </Label>
                  <AvField
                    id="customer-address1"
                    type="text"
                    name="address1"
                    validate={{
                      required: { value: true, errorMessage: 'This field is required.' },
                      minLength: { value: 1, errorMessage: 'This field is required to be at least 1 characters.' },
                      maxLength: { value: 100, errorMessage: 'This field cannot be longer than 100 characters.' }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="address2Label" for="address2">
                    Address 2
                  </Label>
                  <AvField
                    id="customer-address2"
                    type="text"
                    name="address2"
                    validate={{
                      maxLength: { value: 100, errorMessage: 'This field cannot be longer than 100 characters.' }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="address3Label" for="address3">
                    Address 3
                  </Label>
                  <AvField
                    id="customer-address3"
                    type="text"
                    name="address3"
                    validate={{
                      maxLength: { value: 100, errorMessage: 'This field cannot be longer than 100 characters.' }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="townLabel" for="town">
                    Town
                  </Label>
                  <AvField
                    id="customer-town"
                    type="text"
                    name="town"
                    validate={{
                      required: { value: true, errorMessage: 'This field is required.' },
                      minLength: { value: 1, errorMessage: 'This field is required to be at least 1 characters.' },
                      maxLength: { value: 50, errorMessage: 'This field cannot be longer than 50 characters.' }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="postCodeLabel" for="postCode">
                    Post Code
                  </Label>
                  <AvField
                    id="customer-postCode"
                    type="text"
                    name="postCode"
                    validate={{
                      required: { value: true, errorMessage: 'This field is required.' },
                      minLength: { value: 1, errorMessage: 'This field is required to be at least 1 characters.' },
                      maxLength: { value: 20, errorMessage: 'This field cannot be longer than 20 characters.' }
                    }}
                  />
                </AvGroup>
              </Col>
              <Col xs={6}>
                <Row>
                  <Col>
                    <AvGroup>
                      <Label id="titleLabel" for="title">
                        Title
                      </Label>
                      <AvField
                        id="customer-title"
                        type="text"
                        name="title"
                        validate={{
                          maxLength: { value: 100, errorMessage: 'This field cannot be longer than 100 characters.' }
                        }}
                      />
                    </AvGroup>
                  </Col>
                  <Col>
                    <AvGroup>
                      <Label id="firstNameLabel" for="firstName">
                        First Name
                      </Label>
                      <AvField
                        id="customer-firstName"
                        type="text"
                        name="firstName"
                        validate={{
                          maxLength: { value: 100, errorMessage: 'This field cannot be longer than 100 characters.' }
                        }}
                      />
                    </AvGroup>
                  </Col>
                  <Col>
                    <AvGroup>
                      <Label id="lastNameLabel" for="lastName">
                        Last Name
                      </Label>
                      <AvField
                        id="customer-lastName"
                        type="text"
                        name="lastName"
                        validate={{
                          maxLength: { value: 100, errorMessage: 'This field cannot be longer than 100 characters.' }
                        }}
                      />
                    </AvGroup>
                  </Col>
                </Row>
                <Row>
                  <Col>
                    <AvGroup>
                      <Label id="telLabel" for="tel">
                        Tel
                      </Label>
                      <AvField
                        id="customer-tel"
                        type="text"
                        name="tel"
                        validate={{
                          maxLength: { value: 20, errorMessage: 'This field cannot be longer than 20 characters.' }
                        }}
                      />
                    </AvGroup>
                  </Col>
                  <Col>
                    <AvGroup>
                      <Label id="mobileLabel" for="mobile">
                        Mobile
                      </Label>
                      <AvField id="customer-mobile" type="text" name="mobile" />
                    </AvGroup>
                  </Col>
                </Row>
                <AvGroup>
                  <Label id="emailLabel" for="email">
                    Email
                  </Label>
                  <AvField
                    id="customer-email"
                    type="text"
                    name="email"
                    validate={{
                      maxLength: { value: 50, errorMessage: 'This field cannot be longer than 50 characters.' }
                    }}
                  />
                </AvGroup>
              </Col>
              <Col>
                <AvGroup>
                  <Label id="leadLabel">Lead</Label>
                  <AvInput
                    id="customer-lead"
                    type="select"
                    className="form-control"
                    name="lead"
                    value={(!isNew && customerEntity.lead) || ''}
                  >
                    <option value="WEBSITE">Website</option>
                    <option value="DAMP_PROOFER">Damp Proofer</option>
                    <option value="OTHER">Other</option>
                    <option value="YELLOW_PAGES">Yellow Pages</option>
                    <option value="" />
                  </AvInput>
                </AvGroup>
                <AvGroup>
                  <Label id="leadNameLabel" for="leadName">
                    Lead Name
                  </Label>
                  <AvField
                    id="customer-leadname"
                    type="text"
                    name="leadName"
                    validate={{
                      maxLength: { value: 100, errorMessage: 'This field cannot be longer than 100 characters.' }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="leadTelLabel" for="leadTel">
                    Lead Tel
                  </Label>
                  <AvField
                    id="customer-leadtel"
                    type="text"
                    name="leadTel"
                    validate={{
                      maxLength: { value: 100, errorMessage: 'This field cannot be longer than 100 characters.' }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="leadMobLabel" for="leadMob">
                    Lead Mob
                  </Label>
                  <AvField
                    id="customer-leadmob"
                    type="text"
                    name="leadMob"
                    validate={{
                      maxLength: { value: 100, errorMessage: 'This field cannot be longer than 100 characters.' }
                    }}
                  />
                </AvGroup>

                <AvGroup>
                  <Label id="statusLabel">Status</Label>
                  <AvInput
                    id="customer-status"
                    type="select"
                    className="form-control"
                    name="status"
                    value={(!isNew && customerEntity.status) || ''}
                  >
                    <option value="ENQUIRY">ENQUIRY</option>
                    <option value="SALE">SALE</option>
                    <option value="" />
                  </AvInput>
                </AvGroup>

                <Col>
                  <AvGroup>
                    <Label id="enquiryPropertyLabel" for="enquiryProperty">
                      Property
                    </Label>
                    <AvField
                      id="customer-enquiryProperty"
                      type="text"
                      name="enquiryProperty"
                      validate={{
                        maxLength: { value: 100, errorMessage: 'This field cannot be longer than 100 characters.' }
                      }}
                    />
                  </AvGroup>
                  <AvGroup>
                    <Label id="enquiryUnitPqLabel" for="enquiryUnitPq">
                      Unit P/Q
                    </Label>
                    <AvField
                      id="customer-enquiryUnitPq"
                      type="text"
                      name="enquiryUnitPq"
                      validate={{
                        maxLength: { value: 100, errorMessage: 'This field cannot be longer than 100 characters.' }
                      }}
                    />
                  </AvGroup>
                  <AvGroup>
                    <Label id="enquiryInstPqLabel" for="enquiryInstPq">
                      Inst P/Q
                    </Label>
                    <AvField
                      id="customer-enquiryInstPq"
                      type="text"
                      name="enquiryInstPq"
                      validate={{
                        maxLength: { value: 100, errorMessage: 'This field cannot be longer than 100 characters.' }
                      }}
                    />
                  </AvGroup>
                </Col>
                <Col>
                  <AvGroup>
                    <Label id="saleProductsLabel" for="saleProducts">
                      Products
                    </Label>
                    <AvField
                      id="customer-saleProducts"
                      type="text"
                      name="saleProducts"
                      validate={{
                        maxLength: { value: 100, errorMessage: 'This field cannot be longer than 100 characters.' }
                      }}
                    />
                  </AvGroup>
                  <AvGroup>
                    <Label id="saleInvoiceDateLabel" for="saleInvoiceDate">
                      Invoice Date
                    </Label>
                    <AvField
                      id="customer-saleInvoiceDate"
                      type="text"
                      name="saleInvoiceDate"
                      validate={{
                        maxLength: { value: 100, errorMessage: 'This field cannot be longer than 100 characters.' }
                      }}
                    />
                  </AvGroup>
                  <AvGroup>
                    <Label id="saleInvoiceNumberLabel" for="saleInvoiceNumber">
                      Invoice Number
                    </Label>
                    <AvField
                      id="customer-saleInvoiceNumber"
                      type="text"
                      name="saleInvoiceNumber"
                      validate={{
                        maxLength: { value: 100, errorMessage: 'This field cannot be longer than 100 characters.' }
                      }}
                    />
                  </AvGroup>
                  <AvGroup>
                    <Label id="saleInvoiceAmountLabel" for="saleAmountProducts">
                      Amount
                    </Label>
                    <AvField
                      id="customer-saleInvoiceAmount"
                      type="text"
                      name="saleInvoiceAmount"
                      validate={{
                        maxLength: { value: 100, errorMessage: 'This field cannot be longer than 100 characters.' }
                      }}
                    />
                  </AvGroup>
                </Col>
              </Col>
            </Row>
            <Row>
              <Col>
                <AvGroup>
                  <Label id="notesLabel" for="notes">
                    Notes
                  </Label>
                  <AvInput id="customer-notes" type="textarea" name="notes" rows="15" />
                </AvGroup>
                <Button tag={Link} id="cancel-save" to="/entity/domestic" replace color="info">
                  <FontAwesomeIcon icon="arrow-left" />
                  &nbsp;
                  <span className="d-none d-md-inline">Back</span>
                </Button>
                &nbsp;
                <Button color="primary" id="save-entity" type="submit" disabled={updating}>
                  <FontAwesomeIcon icon="save" />
                  &nbsp; Save
                </Button>
              </Col>
            </Row>
          </AvForm>
        )}
      </div>
    );
  }
}

const mapStateToProps = (storeState: IRootState) => ({
  customerEntity: storeState.customer.entity,
  loading: storeState.customer.loading,
  updating: storeState.customer.updating,
  updateSuccess: storeState.customer.updateSuccess
});

const mapDispatchToProps = {
  getEntity,
  updateEntity,
  setBlob,
  createEntity,
  reset
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(CustomerUpdate);
