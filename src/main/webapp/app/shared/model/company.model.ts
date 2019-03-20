export interface ICompany {
  id?: number;
  name?: string;
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
}

export const defaultValue: Readonly<ICompany> = {};
