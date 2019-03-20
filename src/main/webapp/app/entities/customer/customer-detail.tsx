import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
// tslint:disable-next-line:no-unused-variable
import { ICrudGetAction } from 'react-jhipster';
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
      <Row>
        <Col md="8">
          <h2>
            Customer [<b>{customerEntity.id}</b>]
          </h2>
          <dl className="jh-entity-details">
            <dt>
              <span id="companyName">Company Name</span>
            </dt>
            <dd>{customerEntity.companyName}</dd>
            <dt>
              <span id="address1">Address 1</span>
            </dt>
            <dd>{customerEntity.address1}</dd>
            <dt>
              <span id="address2">Address 2</span>
            </dt>
            <dd>{customerEntity.address2}</dd>
            <dt>
              <span id="address3">Address 3</span>
            </dt>
            <dd>{customerEntity.address3}</dd>
            <dt>
              <span id="town">Town</span>
            </dt>
            <dd>{customerEntity.town}</dd>
            <dt>
              <span id="postCode">Post Code</span>
            </dt>
            <dd>{customerEntity.postCode}</dd>
            <dt>
              <span id="title">Title</span>
            </dt>
            <dd>{customerEntity.title}</dd>
            <dt>
              <span id="firstName">First Name</span>
            </dt>
            <dd>{customerEntity.firstName}</dd>
            <dt>
              <span id="lastName">Last Name</span>
            </dt>
            <dd>{customerEntity.lastName}</dd>
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
          <Button tag={Link} to="/entity/customer" replace color="info">
            <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
          </Button>
          &nbsp;
          <Button tag={Link} to={`/entity/customer/${customerEntity.id}/edit`} replace color="primary">
            <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
          </Button>
        </Col>
      </Row>
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
