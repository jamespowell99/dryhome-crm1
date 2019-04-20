import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
// tslint:disable-next-line:no-unused-variable
import { ICrudGetAction, byteSize } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from '../customer.reducer';
import { ICustomer } from 'app/shared/model/customer.model';
// tslint:disable-next-line:no-unused-variable
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface ICustomerDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export class CustomerDetail extends React.Component<ICustomerDetailProps> {
  componentDidMount() {
    this.props.getEntity(this.props.match.params.id);
  }

  render() {
    const { customerEntity } = this.props;
    return (
      <div>
        <Row>
          <Col>
            <h2>
              Domestic [<b>{customerEntity.id}</b>]
            </h2>
          </Col>
        </Row>
        <Row>
          <Col>
            <dl>
              <dt>
                <span id="title">Name</span>
              </dt>
              <dd>
                {customerEntity.title} {customerEntity.firstName} {customerEntity.lastName}
              </dd>
              <dt>
                <span id="address1">Address</span>
              </dt>
              <dd>{customerEntity.address1}</dd>
              <dd>{customerEntity.address2}</dd>
              <dd>{customerEntity.address3}</dd>
              <dd>{customerEntity.town}</dd>
              <dd>{customerEntity.postCode}</dd>
            </dl>
          </Col>
          <Col>
            <dl>
              <dt>
                <span id="tel">Tel</span>
              </dt>
              <dd>{customerEntity.tel}</dd>
              <dt>
                <span id="mobile">Mobile</span>
              </dt>
              <dd>{customerEntity.mobile}</dd>
              <dt>
                <span id="email">Email</span>
              </dt>
              <dd>{customerEntity.email}</dd>
            </dl>
          </Col>
          <Col>
            <dl>
              <dt>
                <span id="type">Lead</span>
              </dt>
              <dd>{customerEntity.lead}</dd>
              <dt>
                <span id="type">Lead Name</span>
              </dt>
              <dd>{customerEntity.leadName}</dd>
              <dt>
                <span id="type">Lead Tel</span>
              </dt>
              <dd>{customerEntity.leadTel}</dd>
              <dt>
                <span id="type">Lead Mob</span>
              </dt>
              <dd>{customerEntity.leadMob}</dd>

              <dt>
                <span id="type">Status</span>
              </dt>
              <dd>{customerEntity.status}</dd>
              <Col>
                <dt>
                  <span id="type">Property</span>
                </dt>
                <dd>{customerEntity.enquiryProperty}</dd>
                <dt>
                  <span id="type">Unit P/Q</span>
                </dt>
                <dd>{customerEntity.enquiryUnitPq}</dd>
                <dt>
                  <span id="type">Inst P/Q</span>
                </dt>
                <dd>{customerEntity.enquiryInstPq}</dd>
              </Col>
              <Col>
                <dt>
                  <span id="type">Products</span>
                </dt>
                <dd>{customerEntity.saleProducts}</dd>
                <dt>
                  <span id="type">Invoice Date</span>
                </dt>
                <dd>{customerEntity.saleInvoiceDate}</dd>
                <dt>
                  <span id="type">Invoice Number</span>
                </dt>
                <dd>{customerEntity.saleInvoiceNumber}</dd>
                <dt>
                  <span id="type">Amount</span>
                </dt>
                <dd>{customerEntity.saleInvoiceAmount}</dd>
              </Col>
            </dl>
          </Col>
        </Row>
        <Row>
          <dl>
            <dt>
              {' '}
              <span id="notes">Notes</span>
            </dt>
            <dd>
              <textarea id="customer-notes" readOnly name="notes" rows={15} cols={100} value={customerEntity.notes} />
            </dd>
          </dl>
        </Row>
        <Row>
          <Button tag={Link} to="/entity/domestic" replace color="info">
            <FontAwesomeIcon icon="arrow-left" />
            <span className="d-none d-md-inline">Back</span>
          </Button>
          &nbsp;
          <Button tag={Link} to={`/entity/domestic/${customerEntity.id}/edit`} replace color="primary">
            <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
          </Button>
        </Row>
      </div>
    );
  }
}

const mapStateToProps = ({ customer }: IRootState) => ({
  customerEntity: customer.entity
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(CustomerDetail);
