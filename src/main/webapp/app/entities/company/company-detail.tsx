import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
// tslint:disable-next-line:no-unused-variable
import { ICrudGetAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './company.reducer';
import { ICompany } from 'app/shared/model/company.model';
// tslint:disable-next-line:no-unused-variable
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface ICompanyDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export class CompanyDetail extends React.Component<ICompanyDetailProps> {
  componentDidMount() {
    this.props.getEntity(this.props.match.params.id);
  }

  render() {
    const { companyEntity } = this.props;
    return (
      <Row>
        <Col md="8">
          <h2>
            Company [<b>{companyEntity.id}</b>]
          </h2>
          <dl className="jh-entity-details">
            <dt>
              <span id="companyName">Company Name</span>
            </dt>
            <dd>{companyEntity.companyName}</dd>
            <dt>
              <span id="address1">Address 1</span>
            </dt>
            <dd>{companyEntity.address1}</dd>
            <dt>
              <span id="address2">Address 2</span>
            </dt>
            <dd>{companyEntity.address2}</dd>
            <dt>
              <span id="address3">Address 3</span>
            </dt>
            <dd>{companyEntity.address3}</dd>
            <dt>
              <span id="town">Town</span>
            </dt>
            <dd>{companyEntity.town}</dd>
            <dt>
              <span id="postCode">Post Code</span>
            </dt>
            <dd>{companyEntity.postCode}</dd>
            <dt>
              <span id="title">Title</span>
            </dt>
            <dd>{companyEntity.title}</dd>
            <dt>
              <span id="firstName">First Name</span>
            </dt>
            <dd>{companyEntity.firstName}</dd>
            <dt>
              <span id="lastName">Last Name</span>
            </dt>
            <dd>{companyEntity.lastName}</dd>
            <dt>
              <span id="tel">Tel</span>
            </dt>
            <dd>{companyEntity.tel}</dd>
            <dt>
              <span id="mobile">Mobile</span>
            </dt>
            <dd>{companyEntity.mobile}</dd>
            <dt>
              <span id="email">Email</span>
            </dt>
            <dd>{companyEntity.email}</dd>
            <dt>
              <span id="products">Products</span>
            </dt>
            <dd>{companyEntity.products}</dd>
            <dt>
              <span id="interested">Interested</span>
            </dt>
            <dd>{companyEntity.interested}</dd>
            <dt>
              <span id="paid">Paid</span>
            </dt>
            <dd>{companyEntity.paid}</dd>
          </dl>
          <Button tag={Link} to="/entity/company" replace color="info">
            <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
          </Button>
          &nbsp;
          <Button tag={Link} to={`/entity/company/${companyEntity.id}/edit`} replace color="primary">
            <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
          </Button>
        </Col>
      </Row>
    );
  }
}

const mapStateToProps = ({ company }: IRootState) => ({
  companyEntity: company.entity
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(CompanyDetail);
