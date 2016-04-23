class Bath < ActiveRecord::Base
  def formatted_date
    self.created_at.to_formatted_s(:simple)
  end
end
