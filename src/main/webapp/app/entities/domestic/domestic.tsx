import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Col, Container, InputGroup, Row, Table } from 'reactstrap';
import { AvForm, AvGroup, AvInput } from 'availity-reactstrap-validation';
// tslint:disable-next-line:no-unused-variable
import { getPaginationItemsNumber, IPaginationBaseState, JhiPagination } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { getSortState } from 'app/shared/util/dryhome-pagination-utils';

import { IRootState } from 'app/shared/reducers';
import { getEntities, getSearchEntities } from 'app/entities/domestic/domestic.reducer';
// tslint:disable-next-line:no-unused-variable
import { ITEMS_PER_PAGE } from 'app/shared/util/pagination.constants';

export interface ICustomerProps extends StateProps, DispatchProps, RouteComponentProps<{ url: string }> {}

export interface ICustomerState extends IPaginationBaseState {
  searchId: string;
  searchTown: string;
  searchPostCode: string;
  searchLastName: string;
  searchTel: string;
  searchMob: string;
}

export class Customer extends React.Component<ICustomerProps, ICustomerState> {
  state: ICustomerState = {
    searchId: '',
    searchTown: '',
    searchPostCode: '',
    searchLastName: '',
    searchTel: '',
    searchMob: '',
    ...getSortState(this.props.location, ITEMS_PER_PAGE)
  };

  componentDidMount() {
    this.getEntities();
  }

  search = () => {
    if (
      this.state.searchId ||
      this.state.searchTown ||
      this.state.searchPostCode ||
      this.state.searchLastName ||
      this.state.searchTel ||
      this.state.searchMob
    ) {
      this.setState({ activePage: 1 }, () => {
        const {
          activePage,
          itemsPerPage,
          sort,
          order,
          searchId,
          searchTown,
          searchPostCode,
          searchLastName,
          searchTel,
          searchMob
        } = this.state;
        this.props.getSearchEntities(
          searchId,
          searchTown,
          searchPostCode,
          searchLastName,
          searchTel,
          searchMob,
          'DOMESTIC',
          activePage - 1,
          itemsPerPage,
          `${sort},${order}`
        );
      });
    }
  };

  clear = () => {
    this.setState(
      {
        searchId: '',
        searchTown: '',
        searchPostCode: '',
        searchLastName: '',
        searchTel: '',
        searchMob: '',
        activePage: 1,
        sort: 'id',
        order: 'asc'
      },
      () => {
        this.props.getEntities('DOMESTIC', 0, ITEMS_PER_PAGE, 'id,desc');
      }
    );
  };

  handleSearchIdChange = event => this.setState({ searchId: event.target.value });
  handleSearchTownChange = event => this.setState({ searchTown: event.target.value });
  handleSearchPostCodeChange = event => this.setState({ searchPostCode: event.target.value });
  handleSearchLastNameChange = event => this.setState({ searchLastName: event.target.value });
  handleSearchTelChange = event => this.setState({ searchTel: event.target.value });
  handleSearchMobChange = event => this.setState({ searchMob: event.target.value });

  sort = prop => () => {
    this.setState(
      {
        order: this.state.order === 'asc' ? 'desc' : 'asc',
        sort: prop
      },
      () => this.sortEntities()
    );
  };

  sortEntities() {
    this.getEntities();
    this.props.history.push(`${this.props.location.pathname}?page=${this.state.activePage}&sort=${this.state.sort},${this.state.order}`);
  }

  handlePagination = activePage => this.setState({ activePage }, () => this.sortEntities());

  getEntities = () => {
    const {
      activePage,
      itemsPerPage,
      sort,
      order,
      searchId,
      searchTown,
      searchPostCode,
      searchLastName,
      searchTel,
      searchMob
    } = this.state;
    if (searchId || searchTown || searchPostCode || searchLastName || searchTel || searchMob) {
      this.props.getSearchEntities(
        searchId,
        searchTown,
        searchPostCode,
        searchLastName,
        searchTel,
        searchMob,
        'DAMP_PROOFER',
        activePage - 1,
        itemsPerPage,
        `${sort},${order}`
      );
    } else {
      this.props.getEntities('DOMESTIC', activePage - 1, itemsPerPage, `${sort},${order}`);
    }
  };

  render() {
    const { customerList, match, totalItems } = this.props;
    return (
      <div>
        <h2 id="customer-heading">
          Domestics
          <Link to={`${match.url}/new`} className="btn btn-primary float-right jh-create-entity" id="jh-create-entity">
            <FontAwesomeIcon icon="plus" />
            &nbsp; Create new Domestic
          </Link>
        </h2>
        <Row>
          <Col sm="12">
            <AvForm onSubmit={this.search} className="border">
              <span>Search</span>
              <AvGroup>
                <Container>
                  <Row>
                    <Col>
                      <AvInput
                        type="text"
                        name="searchId"
                        value={this.state.searchId}
                        onChange={this.handleSearchIdChange}
                        placeholder="ID"
                        className="m-1"
                      />
                      <AvInput
                        type="text"
                        name="searchLastName"
                        value={this.state.searchLastName}
                        onChange={this.handleSearchLastNameChange}
                        placeholder="Last Name"
                        className="m-1"
                      />
                    </Col>
                    <Col>
                      <AvInput
                        type="text"
                        name="searchTown"
                        value={this.state.searchTown}
                        onChange={this.handleSearchTownChange}
                        placeholder="Town"
                        className="m-1"
                      />
                      <AvInput
                        type="text"
                        name="searchTel"
                        value={this.state.searchTel}
                        onChange={this.handleSearchTelChange}
                        className="m-1"
                        placeholder="Tel"
                      />
                    </Col>
                    <Col>
                      <AvInput
                        type="text"
                        name="searchPostCode"
                        value={this.state.searchPostCode}
                        onChange={this.handleSearchPostCodeChange}
                        className="m-1"
                        placeholder="Post Code"
                      />

                      <AvInput
                        type="text"
                        name="searchMob"
                        value={this.state.searchMob}
                        onChange={this.handleSearchMobChange}
                        placeholder="Mob"
                        className="m-1"
                      />
                    </Col>
                  </Row>
                  <Button className="input-group-addon mx-1">
                    <FontAwesomeIcon icon="search" />
                  </Button>
                  <Button type="reset" className="input-group-addon" onClick={this.clear}>
                    <FontAwesomeIcon icon="trash" />
                  </Button>
                  <Row>
                    <span>{totalItems} Records</span>
                  </Row>
                </Container>
              </AvGroup>
            </AvForm>
          </Col>
        </Row>
        <div className="table-responsive table-sm mt-3">
          <Table responsive>
            <thead className="thead-light">
              <tr>
                <th className="hand" onClick={this.sort('id')}>
                  ID <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={this.sort('firstName')}>
                  First Name <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={this.sort('lastName')}>
                  Last Name <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={this.sort('address1')}>
                  Address 1 <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={this.sort('town')}>
                  Town <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={this.sort('postCode')}>
                  Post Code <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={this.sort('tel')}>
                  Tel <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={this.sort('mobile')}>
                  Mobile <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={this.sort('email')}>
                  Email <FontAwesomeIcon icon="sort" />
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {customerList.map((customer, i) => (
                <tr key={`entity-${i}`}>
                  <td>
                    <Button tag={Link} to={`${match.url}/${customer.id}`} color="link" size="sm">
                      {customer.id}
                    </Button>
                  </td>
                  <td>{customer.firstName}</td>
                  <td>{customer.lastName}</td>
                  <td>{customer.address1}</td>
                  <td>{customer.town}</td>
                  <td>{customer.postCode}</td>
                  <td>{customer.tel}</td>
                  <td>{customer.mobile}</td>
                  <td>{customer.email}</td>
                  <td className="text-right">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`${match.url}/${customer.id}/edit`} color="primary" size="sm">
                        <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${customer.id}/delete`} color="danger" size="sm">
                        <FontAwesomeIcon icon="trash" /> <span className="d-none d-md-inline">Delete</span>
                      </Button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>
        </div>
        <Row className="justify-content-center">
          <JhiPagination
            items={getPaginationItemsNumber(totalItems, this.state.itemsPerPage)}
            activePage={this.state.activePage}
            onSelect={this.handlePagination}
            maxButtons={5}
          />
        </Row>
      </div>
    );
  }
}

const mapStateToProps = ({ customer }: IRootState) => ({
  customerList: customer.entities,
  totalItems: customer.totalItems
});

const mapDispatchToProps = {
  getSearchEntities,
  getEntities
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(Customer);
