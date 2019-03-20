export const enum CompanyType {
  DAMP_PROOFER = 'DAMP_PROOFER',
  DOMESTIC = 'DOMESTIC'
}

export interface ICustomer {
  id?: number;
  companyName?: string;
  address1?: string;
  address2?: string;
  address3?: string;
  town?: string;
  postCode?: string;
  title?: string;
  firstName?: string;
  lastName?: string;
  tel?: string;
  mobile?: string;
  email?: string;
  products?: string;
  interested?: string;
  paid?: number;
  type?: CompanyType;
}

export const defaultValue: Readonly<ICustomer> = {};
