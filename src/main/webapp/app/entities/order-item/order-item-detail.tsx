import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
// tslint:disable-next-line:no-unused-variable
import { ICrudGetAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './order-item.reducer';
import { IOrderItem } from 'app/shared/model/order-item.model';
// tslint:disable-next-line:no-unused-variable
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IOrderItemDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export class OrderItemDetail extends React.Component<IOrderItemDetailProps> {
  componentDidMount() {
    this.props.getEntity(this.props.match.params.id);
  }

  render() {
    const { orderItemEntity } = this.props;
    return (
      <Row>
        <Col md="8">
          <h2>
            OrderItem [<b>{orderItemEntity.id}</b>]
          </h2>
          <dl className="jh-entity-details">
            <dt>
              <span id="price">Price</span>
            </dt>
            <dd>{orderItemEntity.price}</dd>
            <dt>
              <span id="quantity">Quantity</span>
            </dt>
            <dd>{orderItemEntity.quantity}</dd>
            <dt>
              <span id="notes">Notes</span>
            </dt>
            <dd>{orderItemEntity.notes}</dd>
            <dt>
              <span id="serialNumber">Serial Number</span>
            </dt>
            <dd>{orderItemEntity.serialNumber}</dd>
            <dt>Product</dt>
            <dd>{orderItemEntity.product ? orderItemEntity.product.name : ''}</dd>
          </dl>
          <Button tag={Link} to="/entity/order-item" replace color="info">
            <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
          </Button>
          &nbsp;
          <Button tag={Link} to={`/entity/order-item/${orderItemEntity.id}/edit`} replace color="primary">
            <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
          </Button>
        </Col>
      </Row>
    );
  }
}

const mapStateToProps = ({ orderItem }: IRootState) => ({
  orderItemEntity: orderItem.entity
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(OrderItemDetail);
