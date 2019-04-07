import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
// tslint:disable-next-line:no-unused-variable
import { ICrudGetAction, byteSize } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './customer.reducer';
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
              Customer [<b>{customerEntity.id}</b>]
            </h2>
            <dl className="jh-entity-details">
              <dt>
                <span id="type">Type</span>
              </dt>
              <dd>{customerEntity.type}</dd>
            </dl>
          </Col>
        </Row>
        <Row>
          <Col>
            <dl>
              <dt>
                <span id="companyName">Company Name</span>
              </dt>
              <dd>{customerEntity.companyName}</dd>

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
                <span id="title">Name</span>
              </dt>
              <dd>
                {customerEntity.title} {customerEntity.firstName} {customerEntity.lastName}
              </dd>
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
                <span id="products">Products</span>
              </dt>
              <dd>{customerEntity.products}</dd>
              <dt>
                <span id="interested">Interested</span>
              </dt>
              <dd>{customerEntity.interested}</dd>
              <dt>
                <span id="paid">Paid</span>
              </dt>
              <dd>{customerEntity.paid}</dd>
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
          <Button tag={Link} to="/entity/customer" replace color="info">
            <FontAwesomeIcon icon="arrow-left" />
            <span className="d-none d-md-inline">Back</span>
          </Button>
          &nbsp;
          <Button tag={Link} to={`/entity/customer/${customerEntity.id}/edit`} replace color="primary">
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
